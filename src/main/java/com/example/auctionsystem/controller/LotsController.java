package com.example.auctionsystem.controller;

import com.example.auctionsystem.dto.BidDTO;
import com.example.auctionsystem.dto.CreateLotDTO;
import com.example.auctionsystem.dto.FullLotDTO;
import com.example.auctionsystem.dto.LotDTO;
import com.example.auctionsystem.model.Lot;
import com.example.auctionsystem.model.status.LotStatus;
import com.example.auctionsystem.projections.FrequentView;
import com.example.auctionsystem.service.LotService;
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

    /*
    Метод создания нового лота, если есть ошибки в полях объекта лота - то нужно вернуть статус с ошибкой
    принимает дто createlot, отдает лот lot

     */
    @PostMapping("/lot")
    public ResponseEntity<LotDTO> createNewLot( @RequestBody CreateLotDTO createLotDTO){
        Lot foundedLot = lotService.createLot(createLotDTO);
        if(foundedLot == null){
            ResponseEntity.status(400).build();
        }
        LotDTO dto = LotDTO.fromLot(foundedLot);
        return ResponseEntity.ok(dto);
    }

    /*
    Переводит лот в состояние "начато", которое позволяет делать ставки на лот.
    Если лот уже находится в состоянии "начато", то ничего не делает и возвращает 200
     */
    @PostMapping("/lot/{id}/start")
    public ResponseEntity<?> startBargain(@PathVariable Long id){
        if (lotService.findLotById(id).getStatus().equals(LotStatus.STARTED.toString())){
            return ResponseEntity.ok().build();
        }
        // если нужный лот со статусом создано, то вернем статус ок
        if(lotService.findLotById(id).getStatus().equals(LotStatus.CREATED.toString())){
            lotService.startBargain(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }else {
        // если статус лота другой, напишем что не найдено
            return ResponseEntity.notFound().build();
        }
    }

    /*
    Создает новую ставку по лоту. Если лот в статусе CREATED или STOPPED, то должна вернутся ошибка
    принимает id лота и имя ставочника, ничо не отдает.
     */
    @PostMapping("/lot/{id}/bid")
    public ResponseEntity<?> createBid( @PathVariable Long id, @RequestBody String name){
        // если лота с таким id не существует, вернем статус не найдено
        if(lotService.findLotById(id) == null){
            return ResponseEntity.notFound().build();
        }

        // если лот в статусе создано, то создаем бид и возвращаем статус ок
        if(lotService.findLotById(id).getStatus().equals(LotStatus.STARTED.toString())){
            lotService.createBid(id, name);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /*
    Переводит лот в состояние "остановлен", которое запрещает делать ставки на лот.
    Если лот уже находится в состоянии "остановлен", то ничего не делает и возвращает 200
     */
    @PostMapping("/lot/{id}/stop")
    public ResponseEntity<?> stopBargain(@PathVariable Long id){
        if(lotService.findLotById(id) == null){
            return ResponseEntity.notFound().build();
        }

        if(lotService.findLotById(id).getStatus().equals(LotStatus.STOPPED.toString())){
            return ResponseEntity.ok().build();
        }

        if(lotService.findLotById(id).getStatus().equals(LotStatus.STARTED.toString())){
            lotService.stopBargain(id);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
    /*
    Возвращает первого ставившего на этот лот
     */
    @GetMapping("/lot/{id}/first")
    public ResponseEntity<BidDTO> getFirstBidPerson( @PathVariable Long id){
        if(lotService.findLotById(id) == null){
            return ResponseEntity.notFound().build();
        }
        BidDTO founded = lotService.getFirstBidPerson(id);
        return ResponseEntity.ok(founded);
    }
    /*

Возвращает имя ставившего на данный лот наибольшее количество раз
    Наибольшее количество вычисляется из общего количества ставок на лот
     */
    @GetMapping("lot/{id}/frequent")
    public ResponseEntity<FrequentView> getFrequentBid( @PathVariable Long id){
        if(lotService.findLotById(id) == null){
            return ResponseEntity.notFound().build();
        }
        FrequentView founded = lotService.getFrequentBid(id);
        return ResponseEntity.ok(founded);
    }
    /*
    Возвращает полную информацию о лоте с последним ставившим и текущей ценой.
    Принимает айди лота, возвращает фулл лот
     */
    @GetMapping("lot/{id}")
    public ResponseEntity<FullLotDTO> getAllInfoOfLot(@PathVariable Long id){
        if(lotService.getFullLot(id) == null){
            return ResponseEntity.notFound().build();
        }
        FullLotDTO founded = lotService.getFullLot(id);
        return ResponseEntity.ok(founded);
    }
    /*
    Возвращает все записи о лотах без информации о текущей цене и победителе постранично.
    Если страница не указана, то возвращается первая страница.
    Номера страниц начинаются с 0. Лимит на количество лотов на странице - 10 штук.
     */
    @GetMapping("lot")
    public ResponseEntity<List<LotDTO>> getAllLots(@RequestParam("status") String status,
                                                @RequestParam("page") Integer pageNumber){
        List<LotDTO> founded = lotService.getAllLots(status, pageNumber);
        if(founded == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(founded);
    }

    /*
    Экспортировать все лоты в формате id,title,status,lastBidder,currentPrice в одном файле CSV
     */
    @GetMapping("lot/export")
    public void exportLots( HttpServletResponse response ) throws IOException {
       lotService.export(response);
    }

}
