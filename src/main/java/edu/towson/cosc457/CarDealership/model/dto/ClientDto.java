package edu.towson.cosc457.CarDealership.model.dto;

import edu.towson.cosc457.CarDealership.misc.Gender;
import edu.towson.cosc457.CarDealership.model.Client;
import edu.towson.cosc457.CarDealership.model.SalesAssociate;
import lombok.Data;

@Data
public class ClientDto {
    private Long id;
    private Integer ssn;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private String address;
    private SalesAssociate salesAssociate;
    private Double minimumPrice;
    private Double maximumPrice;

    public static ClientDto from (Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setSsn(client.getSsn());
        clientDto.setFirstName(client.getFirstName());
        clientDto.setLastName(client.getLastName());
        clientDto.setGender(client.getGender());
        clientDto.setEmail(client.getEmail());
        clientDto.setPhoneNumber(client.getPhoneNumber());
        clientDto.setAddress(client.getAddress());
        clientDto.setSalesAssociate(client.getSalesAssociate());
        clientDto.setMinimumPrice(client.getMinimumPrice());
        clientDto.setMaximumPrice(client.getMaximumPrice());
        return clientDto;
    }
}