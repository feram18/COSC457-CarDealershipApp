package edu.towson.cosc457.CarDealership.model;

import edu.towson.cosc457.CarDealership.misc.Gender;
import edu.towson.cosc457.CarDealership.misc.Role;
import edu.towson.cosc457.CarDealership.model.dto.ManagerDto;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "MANAGER")
@DiscriminatorValue("MANAGER")
public class Manager extends Employee {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "site_manager_id")
    private SiteManager siteManager;
    @OneToOne(mappedBy = "manager",
                cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    private Department department;
    @OneToMany(mappedBy = "manager")
    private List<Mechanic> mechanics;
    @OneToMany(mappedBy = "manager")
    private List<SalesAssociate> salesAssociates;

    public Manager() { }

    public Manager(Integer ssn,
                   String firstName,
                   Character middleInitial,
                   String lastName,
                   Gender gender,
                   LocalDate dateOfBirth,
                   String phoneNumber,
                   String email,
                   Location workLocation,
                   Double salary,
                   LocalDate dateStarted,
                   String address,
                   Double hoursWorked,
                   Role role,
                   String username,
                   String password,
                   SiteManager siteManager,
                   Department department,
                   List<Mechanic> mechanics,
                   List<SalesAssociate> salesAssociates) {
        super(ssn,
                firstName,
                middleInitial,
                lastName,
                gender,
                dateOfBirth,
                phoneNumber,
                email,
                workLocation,
                salary,
                dateStarted,
                address,
                hoursWorked,
                role,
                username,
                password);
        this.siteManager = siteManager;
        this.department = department;
        this.mechanics = mechanics;
        this.salesAssociates = salesAssociates;
    }

    public void assignMechanics(Mechanic mechanic) {
        mechanics.add(mechanic);
    }

    public void removeMechanics(Mechanic mechanic) {
        mechanics.remove(mechanic);
    }

    public void assignAssociates(SalesAssociate associate) {
        salesAssociates.add(associate);
    }

    public void removeAssociates(SalesAssociate associate) {
        salesAssociates.remove(associate);
    }

    public static Manager from (ManagerDto managerDto) {
        Manager manager = new Manager();
        manager.setSiteManager(managerDto.getSiteManager());
        manager.setDepartment(managerDto.getDepartment());
        manager.setMechanics(managerDto.getMechanicsDto()
                .stream().map(Mechanic::from).collect(Collectors.toList()));
        manager.setSalesAssociates(managerDto.getSalesAssociatesDto()
                .stream().map(SalesAssociate::from).collect(Collectors.toList()));
        return manager;
    }
}