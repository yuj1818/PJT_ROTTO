package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.ReqBoardService;
import com.rezero.rotto.dto.response.ReqBoardDetailRegisterModifyResponse;
import com.rezero.rotto.dto.response.ReqBoardListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/req")
@RequiredArgsConstructor
@Tag(name = "ReqBoard 컨트롤러", description = "문의게시판 기능을 위한 API")
public class ReqBoardController {

    private final ReqBoardService reqBoardService;

    @Operation(summary = "문의게시판 목록 조회",
            description = "문의게시판 전체 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ReqBoardListResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패")
    })
    @GetMapping
    public ResponseEntity<?> getReqBoardList(int userCode) {
        return reqBoardService.getReqBoardList(userCode);
    }


    @Operation(summary = "문의게시판 상세 조회",
            description = "문의게시판 상세 조회해볼까요?")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = ReqBoardDetailRegisterModifyResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })

    @GetMapping("/{req-board-code}")
    public ResponseEntity<?> getReqBoardDetail(int userCode, int reqBoardCode) {
        return reqBoardService.getReqBoardDetail(userCode, reqBoardCode);
    }


    @Operation(summary = "문의게시글 생성",
            description = "문의게시글을 생성해볼까요?")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "게시글 생성 성공",
                    content = @Content(schema = @Schema(implementation = ReqBoardDetailRegisterModifyResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않은 게시글")
    })

    @PostMapping("/{req-board-code}")
    public  ResponseEntity<?> postReqBoard(int userCode, ReqBoardDetailRegisterModifyResponse req){
        return reqBoardService.postReqBoard(userCode, req);
    }
}
