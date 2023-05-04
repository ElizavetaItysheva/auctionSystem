package com.example.auctionsystem.controller;

import com.example.auctionsystem.dto.BidDTO;
import com.example.auctionsystem.dto.CreateLotDTO;
import com.example.auctionsystem.dto.FullLotDTO;
import com.example.auctionsystem.dto.LotDTO;

import com.example.auctionsystem.model.status.LotStatus;
import com.example.auctionsystem.projections.FrequentView;
import com.example.auctionsystem.service.LotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("lots")
public class LotsController {
    private final LotService lotService;

    public LotsController( LotService lotService ) {
        this.lotService = lotService;
    }

    @Operation(summary = "Создание нового лота", tags = "Лоты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LotDTO.class))}),
                    @ApiResponse(responseCode = "400", description = "Not found")
            })
    @PostMapping("/lot")
    public ResponseEntity<LotDTO> createNewLot( @RequestBody CreateLotDTO createLotDTO ) {
        LotDTO foundedLot = lotService.createLot(createLotDTO);
        return ResponseEntity.ok(foundedLot);
    }

    @Operation(summary = "Перевод лота в состояние 'начато'", tags = "Лоты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @PostMapping("/lot/{id}/start")
    public ResponseEntity<?> startBargain( @PathVariable Long id ) {
        String isLotStartedOrCreated = lotService.getStatusOfLot(id);

        if (isLotStartedOrCreated.equals("started")) {
            return ResponseEntity.ok().build();
        }
        if (isLotStartedOrCreated.equals("created")) {
            lotService.startBargain(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Создание бида", tags = "бид",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not found"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    @PostMapping("/lot/{id}/bid")
    public ResponseEntity<?> createBid( @PathVariable Long id, @RequestBody String name ) {
        String isLotStarted = lotService.getStatusOfLot(id);
        if (isLotStarted.equals("started")) {
            lotService.createBid(id, name);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Перевод лота в состояние 'остановлено'", tags = "лоты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not found"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    @PostMapping("/lot/{id}/stop")
    public ResponseEntity<?> stopBargain( @PathVariable Long id ) {
        String statusOfLot = lotService.getStatusOfLot(id);
        if (statusOfLot.equals("stopped")) {
            return ResponseEntity.ok().build();
        }

        if (statusOfLot.equals("started")) {
            lotService.stopBargain(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Получение первого ставившего на лот", tags = "лоты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BidDTO.class))}),
                    @ApiResponse(responseCode = "404", description = "Not found"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    @GetMapping("/lot/{id}/first")
    public ResponseEntity<BidDTO> getFirstBidPerson( @PathVariable Long id ) {
        BidDTO founded = lotService.getFirstBidPerson(id);
        return ResponseEntity.ok(founded);
    }

    @Operation(summary = "Получение персоны с наибольшим количеством ставок", tags = "бид",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("lot/{id}/frequent")
    public ResponseEntity<FrequentView> getFrequentBid( @PathVariable Long id ) {

        FrequentView founded = lotService.getFrequentBid(id);
        return ResponseEntity.ok(founded);
    }

    @Operation(summary = "Получение полной информации о лоте", tags = "лоты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("lot/{id}")
    public ResponseEntity<FullLotDTO> getAllInfoOfLot( @PathVariable Long id ) {

        FullLotDTO founded = lotService.getFullLot(id);
        return ResponseEntity.ok(founded);
    }

    @Operation(summary = "Получение всех лотов без информации о текущей цене и победителе", tags = "лоты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("lot")
    public ResponseEntity<List<LotDTO>> getAllLots( @RequestParam("status") LotStatus status,
                                                    @RequestParam("page") Integer pageNumber ) {
        List<LotDTO> founded = lotService.getAllLots(status, pageNumber);

        return ResponseEntity.ok(founded);
    }

    @Operation(summary = "Экспорт всех лотов в формате CSV файла", tags = "лоты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")
            })
    @GetMapping("lot/export")
    public ResponseEntity<?> exportLots( HttpServletResponse response ) throws IOException {
        lotService.export(response);
        return ResponseEntity.ok().build();
    }

}
