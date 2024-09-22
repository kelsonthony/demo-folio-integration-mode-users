package com.kelsonthony.demofoliointegrationmodeusers.app.api.openapi;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.JobExecutionDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.JobResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;


public interface UserControllerOpenApi {

    @Operation(
            summary = "Inicia o job de importação de relatórios",
            description = "Este endpoint inicia um job para importar relatórios e retorna o status da execução.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Job iniciado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JobResultDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "string")
                            )
                    )
            }
    )
    ResponseEntity<JobExecutionDTO> startBatch();
}
