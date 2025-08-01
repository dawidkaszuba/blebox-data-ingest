package pl.dawidkaszuba.blebox_data_ingest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dawidkaszuba.blebox_data_ingest.config.BleBoxConfig;
import pl.dawidkaszuba.blebox_data_ingest.service.NotificationService;
import pl.dawidkaszuba.blebox_data_ingest.service.PollDataService;

@Slf4j
@RestController
@RequestMapping("/blebox")
public class DataPollController {

    private final NotificationService notificationService;
    private final BleBoxConfig bleBoxConfig;
    private final PollDataService pollDataService;


    public DataPollController(NotificationService notificationService, BleBoxConfig bleBoxConfig, PollDataService pollDataService) {
        this.notificationService = notificationService;
        this.bleBoxConfig = bleBoxConfig;
        this.pollDataService = pollDataService;
    }

    @GetMapping("/poll")
    public ResponseEntity<String> pollAllDevices() {
        pollDataService.pollDevices();
        return ResponseEntity.ok("Polling triggered for all devices");
    }

}
