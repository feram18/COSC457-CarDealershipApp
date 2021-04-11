package edu.towson.cosc457.CarDealership.controller;

import edu.towson.cosc457.CarDealership.model.Client;
import edu.towson.cosc457.CarDealership.model.dto.ClientDto;
import edu.towson.cosc457.CarDealership.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientDto> addClient(@RequestBody final ClientDto clientDto) {
        Client client = clientService.addClient(Client.from(clientDto));
        return new ResponseEntity<>(ClientDto.from(client), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getClients() {
        List<Client> clients = clientService.getClients();
        List<ClientDto> clientsDto = clients.stream().map(ClientDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(clientsDto, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<ClientDto> getClient(@PathVariable final Long id) {
        Client client = clientService.getClient(id);
        return new ResponseEntity<>(ClientDto.from(client), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<ClientDto> deleteClient(@PathVariable final Long id) {
        Client client = clientService.deleteClient(id);
        return new ResponseEntity<>(ClientDto.from(client), HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<ClientDto> editClient(@PathVariable final Long id,
                                                @RequestBody final ClientDto clientDto) {
        Client client = clientService.editClient(id, Client.from(clientDto));
        return new ResponseEntity<>(ClientDto.from(client), HttpStatus.OK);
    }
}
