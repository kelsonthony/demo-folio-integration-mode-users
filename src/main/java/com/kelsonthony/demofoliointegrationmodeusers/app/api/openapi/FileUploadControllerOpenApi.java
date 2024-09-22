//package com.kelsonthony.demofoliointegrationmodeusers.app.api.openapi;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//public interface FileUploadControllerOpenApi {
//
//    @Operation(
//            summary = "Faz o upload de um arquivo XLSX",
//            description = "Este endpoint permite o upload de um arquivo XLSX para o caminho especificado na configuração. " +
//                    "O arquivo deve ser do tipo XLSX e não pode exceder o tamanho máximo permitido.",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    content = @Content(
//                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
//                            schema = @Schema(
//
//
//                            )
//                    )
//            )
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Arquivo carregado com sucesso."),
//            @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique se o arquivo é um XLSX.",
//                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "500", description = "Falha ao carregar o arquivo.",
//                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file);
//}
