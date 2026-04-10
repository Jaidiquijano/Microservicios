package com.aula.microservices.gateway.filter;

import com.aula.microservices.gateway.config.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Filtro JWT que se aplica a todas las rutas protegidas del gateway.
 *
 * Qué hace:
 *  1. Comprueba que la petición lleva un header Authorization: Bearer <token>
 *  2. Valida que el token sea correcto y no esté expirado
 *  3. Si es válido, añade X-User-Email y X-User-Role como headers
 *     para que los microservicios sepan quién es el usuario
 *  4. Si no es válido, devuelve 401 Unauthorized directamente
 */
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final JwtUtil jwtUtil;

    @Value("${app.public-paths}")
    private List<String> publicPaths;

    public AuthFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();

            // Si es una ruta pública, dejar pasar sin validar token
            if (isPublicPath(path)) {
                return chain.filter(exchange);
            }

            // Comprobar que existe el header Authorization
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Token no proporcionado");
            }

            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Formato de token incorrecto. Usa: Bearer <token>");
            }

            String token = authHeader.substring(7);

            if (!jwtUtil.isValid(token)) {
                return onError(exchange, "Token inválido o expirado");
            }

            // Token válido: añadir email y rol como headers internos
            // Los microservicios los leen con @RequestHeader("X-User-Email")
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(r -> r
                            .header("X-User-Email", jwtUtil.getEmail(token))
                            .header("X-User-Role", jwtUtil.getRole(token))
                    )
                    .build();

            return chain.filter(modifiedExchange);
        };
    }

    private boolean isPublicPath(String path) {
        return publicPaths.stream().anyMatch(publicPath -> {
            if (publicPath.endsWith("/**")) {
                String base = publicPath.replace("/**", "");
                return path.startsWith(base);
            }
            return path.equals(publicPath);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders()
                .add(HttpHeaders.CONTENT_TYPE, "application/json");
        byte[] body = ("{\"success\":false,\"message\":\"" + message + "\"}").getBytes();
        var buffer = exchange.getResponse().bufferFactory().wrap(body);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    // Clase de configuración vacía (requerida por AbstractGatewayFilterFactory)
    public static class Config {}
}