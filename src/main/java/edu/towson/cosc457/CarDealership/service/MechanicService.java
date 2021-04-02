package edu.towson.cosc457.CarDealership.service;

import edu.towson.cosc457.CarDealership.exceptions.NotFoundException;
import edu.towson.cosc457.CarDealership.exceptions.AlreadyAssignedException;
import edu.towson.cosc457.CarDealership.misc.EmployeeType;
import edu.towson.cosc457.CarDealership.misc.Entity;
import edu.towson.cosc457.CarDealership.model.Employee;
import edu.towson.cosc457.CarDealership.model.Mechanic;
import edu.towson.cosc457.CarDealership.model.ServiceTicket;
import edu.towson.cosc457.CarDealership.repository.MechanicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MechanicService implements EmployeeService<Mechanic> {
    private final MechanicRepository mechanicRepository;
    private final ServiceTicketService ticketService;

    @Autowired
    public MechanicService(MechanicRepository mechanicRepository, ServiceTicketService ticketService) {
        this.mechanicRepository = mechanicRepository;
        this.ticketService = ticketService;
    }

    @Override
    public Mechanic addEmployee(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }

//    @Override
//    public List<Employee> getEmployees() {
//        return StreamSupport
//                .stream(mechanicRepository.findAll().spliterator(), false)
//                .collect(Collectors.toList());
//    }

    @Override
    public Mechanic getEmployee(Long id) {
        return (Mechanic) mechanicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Entity.MECHANIC.toString(), id));
    }

    @Override
    public Mechanic deleteEmployee(Long id) {
        Mechanic mechanic = getEmployee(id);
        mechanicRepository.delete(mechanic);
        return mechanic;
    }

    @Override
    @Transactional
    public Mechanic editEmployee(Long id, Mechanic mechanic) {
        Mechanic mechanicToEdit = getEmployee(id);
        mechanicToEdit.setSsn(mechanic.getSsn());
        mechanicToEdit.setFirstName(mechanic.getFirstName());
        mechanicToEdit.setMiddleInitial(mechanic.getMiddleInitial());
        mechanicToEdit.setLastName(mechanic.getLastName());
        mechanicToEdit.setGender(mechanic.getGender());
        mechanicToEdit.setDateOfBirth(mechanic.getDateOfBirth());
        mechanicToEdit.setPhoneNumber(mechanic.getPhoneNumber());
        mechanicToEdit.setEmail(mechanic.getEmail());
        mechanicToEdit.setWorkLocation(mechanic.getWorkLocation());
        mechanicToEdit.setSalary(mechanic.getSalary());
        mechanicToEdit.setDateStarted(mechanic.getDateStarted());
        mechanicToEdit.setAddress(mechanic.getAddress());
        mechanicToEdit.setHoursWorked(mechanic.getHoursWorked());
        mechanicToEdit.setRole(mechanic.getRole());
        mechanicToEdit.setUsername(mechanic.getUsername());
        mechanicToEdit.setPassword(mechanic.getPassword());
        mechanicToEdit.setManager(mechanic.getManager());
        mechanicToEdit.setDepartment(mechanic.getDepartment());
        mechanicToEdit.setTickets(mechanic.getTickets());
        mechanicToEdit.setComments(mechanic.getComments());
        return mechanicToEdit;
    }

    @Transactional
    public Mechanic assignTicket(Long mechanicId, Long ticketId) {
        Mechanic mechanic = getEmployee(mechanicId);
        ServiceTicket ticket = ticketService.getServiceTicket(ticketId);
        if (Objects.nonNull(ticket.getMechanic())) {
            throw new AlreadyAssignedException(
                    Entity.SERVICE_TICKET.toString(),
                    ticketId,
                    Entity.MECHANIC.toString(),
                    ticket.getMechanic().getId()
            );
        }
        mechanic.addServiceTicket(ticket);
        ticket.setMechanic(mechanic);
        return mechanic;
    }

    @Transactional
    public Mechanic removeTicket(Long mechanicId, Long ticketId) {
        Mechanic mechanic = getEmployee(mechanicId);
        ServiceTicket ticket = ticketService.getServiceTicket(ticketId);
        mechanic.removeServiceTicket(ticket);
        return mechanic;
    }
}