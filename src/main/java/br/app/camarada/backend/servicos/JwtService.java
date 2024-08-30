package br.app.camarada.backend.servicos;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("3677397A24432646294A404E635266556A586E327234753778214125442A472D4B6150645367566B59703373367638792F423F45284" +
            "82B4D6251655468576D5A7134743777217A24432646294A404E635266556A586E3272357538782F413F442A472D4B615064" +
            "5367566B59703373367639792442264529482B4D6251655468")
    private String segredoDeAssinaturaJwt;

    public String extrairTotemId() {
        return null;
    }
    public String gerarToken(
            UserDetails userDetails
    ) {
        return gerarToken(new HashMap<>(),userDetails);
    }
    public String gerarToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000L ))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 *60 * 24 * 30))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extrairNomeDeUsuario(token);
        return ( username.equals(userDetails.getUsername()) && isTokenNotExpired(token));
    }

    private boolean isTokenNotExpired(String token) {
        return extrairTempoDeExpiracao(token).after(new Date(System.currentTimeMillis()));

    }

    private Date extrairTempoDeExpiracao(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public String extrairNomeDeUsuario(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extrairTodasAsClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extrairTodasAsClaims(String token) {
        JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder();

        jwtParserBuilder.setSigningKey(getSignInKey());
        JwtParser parser = jwtParserBuilder.build();
        return parser.parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(segredoDeAssinaturaJwt);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
