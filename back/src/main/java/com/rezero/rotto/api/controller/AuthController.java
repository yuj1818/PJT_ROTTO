package com.rezero.rotto.api.controller;

import com.rezero.rotto.dto.request.LoginRequest;
import com.rezero.rotto.dto.response.TokenResponse;
import com.rezero.rotto.entity.BlackList;
import com.rezero.rotto.entity.RefreshToken;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.BlackListRepository;
import com.rezero.rotto.repository.RefreshTokenRepository;
import com.rezero.rotto.repository.UserRepository;
import com.rezero.rotto.utils.AESUtil;
import com.rezero.rotto.utils.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth 컨트롤러", description = "로그인&로그아웃 기능을 위한 API")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final SecretKey aesKey;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;


    @Operation(summary = "로그인",
            description = "JWT 토큰 발급 과정 및 암호화 과정을 포함한 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 제공",
            content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String phoneNum = request.getPhoneNum();
        String password = request.getPassword();

        try {
            // 폰 번호 암호화
            String encryptedPhoneNum = AESUtil.encrypt(phoneNum, aesKey);

            // DB 에서 암호화된 폰 번호로 사용자 조회
            User user = userRepository.findByPhoneNum(encryptedPhoneNum)
                    .orElseThrow(() -> new RuntimeException("로그인에 실패하였습니다."));

            // password 해시값 검증
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("로그인에 실패하였습니다.");
            }

            if (user.getIsDelete()) {
                throw new RuntimeException("존재하지 않는 사용자입니다.");
            }
            
            // 액세스 토큰, 리프레시 토큰 발급
            String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(user.getUserCode()));
            String refreshToken = jwtTokenProvider.createRefreshToken();

            RefreshToken ref = new RefreshToken(user.getUserCode(), refreshToken);
            refreshTokenRepository.save(ref);

            TokenResponse tokenResponse = TokenResponse.builder()
                    .grantType("Bearer")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            // 암호화 실패 또는 기타 예외 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 도중 오류 발생.");
        }
    }

    @Operation(summary = "로그아웃",
            description = "JWT 토큰 삭제 후 로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam("accessToken") String accessToken,
                                    @RequestParam("refreshToken") String refreshToken) {
        try {
            // 먼저 토큰의 유효성을 확인
            if (!jwtTokenProvider.validateToken(accessToken) || !jwtTokenProvider.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 토큰입니다.");
            }

            // 토큰을 블랙리스트에 추가
            BlackList access = BlackList.builder()
                    .token(accessToken)
                    .expiration(jwtTokenProvider.getExpiration(accessToken))
                    .build();
            BlackList refresh = BlackList.builder()
                    .token(refreshToken)
                    .expiration(jwtTokenProvider.getExpiration(refreshToken))
                    .build();

            blackListRepository.save(access);
            blackListRepository.save(refresh);

            // 토큰을 저장소에서 삭제
            refreshTokenRepository.deleteById(refreshToken);

            return ResponseEntity.status(HttpStatus.OK).body("로그아웃 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Operation(summary = "토큰 재발급",
            description = "액세스 토큰 만료 시 리프레시 토큰 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "만료된 리프레시 토큰")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Authorization 헤더가 없거나 Bearer 토큰 형식이 아닙니다.");
            }

            String refreshToken = authorizationHeader.substring(7);

            // 토큰 유효성 및 블랙리스트 검사
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                throw new RuntimeException("리프레시 토큰이 유효하지 않거나 블랙리스트에 등록되어 있습니다.");
            }

            String userCode = jwtTokenProvider.getPayload(refreshToken);
            String newAccessToken = jwtTokenProvider.createAccessToken(userCode);

            TokenResponse tokenResponse = TokenResponse.builder()
                    .grantType("Bearer")
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)  // 리프레시 토큰은 재발급하지 않음
                    .build();

            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("오류 발생: " + e.getMessage());
        }
    }
}