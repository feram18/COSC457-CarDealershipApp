package edu.towson.cosc457.CarDealership.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.towson.cosc457.CarDealership.exceptions.NotFoundException;
import edu.towson.cosc457.CarDealership.misc.EmployeeType;
import edu.towson.cosc457.CarDealership.misc.Gender;
import edu.towson.cosc457.CarDealership.model.*;
import edu.towson.cosc457.CarDealership.service.DepartmentService;
import edu.towson.cosc457.CarDealership.service.LocationService;
import edu.towson.cosc457.CarDealership.service.LotService;
import edu.towson.cosc457.CarDealership.service.MechanicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class LocationControllerTest {
    public MockMvc mockMvc;
    @Autowired
    public LocationController locationController;
    @MockBean
    public LocationService locationService;
    @MockBean
    public LotService lotService;
    @MockBean
    public DepartmentService departmentService;
    @MockBean
    public MechanicService mechanicService;
    private final ObjectMapper mapper = new ObjectMapper();
    private Location location;
    private Location editedLocation;
    private Lot lot;
    private Department department;
    private Mechanic mechanic;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);

        location = Location.builder()
                .id(1L)
                .name("Location A")
                .address(Address.builder()
                        .id(1L)
                        .street("123 Main St.")
                        .city("New York City")
                        .state("New York")
                        .zipCode(12345)
                        .build())
                .siteManager(SiteManager.builder()
                        .id(1L)
                        .build())
                .lots(new ArrayList<>())
                .departments(new ArrayList<>())
                .mechanics(new ArrayList<>())
                .build();

        editedLocation = Location.builder()
                .id(1L)
                .name("New Location Name")
                .address(Address.builder()
                        .id(1L)
                        .street("123 Main St.")
                        .city("New York City")
                        .state("New York")
                        .zipCode(12345)
                        .build())
                .siteManager(SiteManager.builder()
                        .id(1L)
                        .build())
                .lots(new ArrayList<>())
                .departments(new ArrayList<>())
                .mechanics(new ArrayList<>())
                .build();

        lot = Lot.builder()
                .id(1L)
                .size(100.15)
                .build();

        department = Department.builder()
                .id(1L)
                .name("Department A")
                .manager(Manager.builder()
                        .id(1L)
                        .build())
                .build();

        mechanic = Mechanic.builder()
                .id(1L)
                .ssn("123-45-6789")
                .firstName("FirstName")
                .middleInitial('M')
                .lastName("LastName")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.now())
                .phoneNumber("123-456-7890")
                .email("mechanic@company.com")
                .salary(45000.00)
                .dateStarted(LocalDate.now())
                .address(Address.builder()
                        .id(1L)
                        .street("123 Main St.")
                        .city("New York City")
                        .state("New York")
                        .zipCode(12345)
                        .build())
                .hoursWorked(780.00)
                .employeeType(EmployeeType.MECHANIC)
                .build();
    }

    @Test
    void shouldAddLocation() throws Exception {
        when(locationService.addLocation(location)).thenReturn(location);

        mockMvc.perform(post("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(location)))
                .andExpect(status().is(201));
    }

    @Test
    void shouldFailToAddLocation_Null() throws Exception {
        mockMvc.perform(post("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(null)))
                .andExpect(status().is(400)); // BAD_REQUEST
    }

    @Test
    void shouldGetAllLocations() throws Exception {
        when(locationService.getLocations()).thenReturn(Collections.singletonList(location));

        mockMvc.perform(get("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldGetAllLocations_EmptyList() throws Exception {
        when(locationService.getLocations()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    void shouldGetLocationById() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/{id}", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldFailToGetLocationById_NotFound() throws Exception {
        when(locationService.getLocation(location.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/locations/{id}", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404));
    }

    @Test
    void shouldDeleteLocation() throws Exception {
        when(locationService.deleteLocation(location.getId())).thenReturn(location);

        mockMvc.perform(delete("/api/v1/locations/{id}", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldFailToDeleteLocation_NotFound() throws Exception {
        when(locationService.deleteLocation(location.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(delete("/api/v1/locations/{id}", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404));
    }

    @Test
    void shouldUpdateLocation() throws Exception {
        when(locationService.editLocation(location.getId(), editedLocation)).thenReturn(editedLocation);

        mockMvc.perform(put("/api/v1/locations/{id}", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(editedLocation)))
                .andExpect(status().is(200));
    }

    @Test
    @Disabled
    void shouldFailToUpdateLocation_NotFound() throws Exception {
        when(locationService.editLocation(location.getId(), editedLocation)).thenThrow(NotFoundException.class);

        mockMvc.perform(put("/api/v1/locations/{id}", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(editedLocation)))
                .andExpect(status().is(400));
    }

    @Test
    void shouldAddLotToLocation() throws Exception {
        when(locationService.addLotToLocation(location.getId(), lot.getId())).thenReturn(location);

        mockMvc.perform(post("/api/v1/locations/{locationId}/lots/{lotId}/add", location.getId(), lot.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldFailToAddLotToLocation_LotNotFound() throws Exception {
        when(locationService.addLotToLocation(location.getId(), lot.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(post("/api/v1/locations/{locationId}/lots/{lotId}/add", location.getId(), lot.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404));
    }

    @Test
    void shouldGetLotsOnLocation() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);
        when(lotService.getLot(lot.getId())).thenReturn(lot);
        when(locationService.addLotToLocation(location.getId(), lot.getId())).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/{id}/lots", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldGetLotsOnLocation_EmptyList() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/{id}/lots", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    void shouldRemoveLotFromLocation() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);
        when(lotService.getLot(lot.getId())).thenReturn(lot);
        when(locationService.addLotToLocation(location.getId(), lot.getId())).thenReturn(location);
        when(locationService.removeLotFromLocation(location.getId(), lot.getId())).thenReturn(location);

        mockMvc.perform(delete("/api/v1/locations/{locationId}/lots/{lotId}/remove",
                location.getId(),
                lot.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldFailToRemoveLotFromLocation_NotFound() throws Exception {
        when(locationService.removeLotFromLocation(location.getId(), lot.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(delete("/api/v1/locations/{locationId}/lots/{lotId}/remove",
                location.getId(),
                lot.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404));
    }

    @Test
    void shouldGetDepartmentsOnLocation() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);
        when(departmentService.getDepartment(department.getId())).thenReturn(department);
        when(locationService.assignMechanic(location.getId(), mechanic.getId())).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/{id}/departments", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldGetDepartmentsOnLocation_EmptyList() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/{id}/departments", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    void shouldAddDepartmentToLocation() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);
        when(departmentService.getDepartment(department.getId())).thenReturn(department);
        when(locationService.addDepartmentToLocation(location.getId(), mechanic.getId())).thenReturn(location);

        mockMvc.perform(post("/api/v1/locations/{id}/departments/{departmentId}/add",
                location.getId(),
                department.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldFailToAddDepartmentToLocation_NotFound() throws Exception {
        when(locationService.addDepartmentToLocation(location.getId(), mechanic.getId()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(post("/api/v1/locations/{id}/departments/{departmentId}/add",
                location.getId(),
                department.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404));
    }

    @Test
    void shouldRemoveDepartmentFromLocation() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);
        when(departmentService.getDepartment(department.getId())).thenReturn(department);
        when(locationService.addDepartmentToLocation(location.getId(), mechanic.getId())).thenReturn(location);
        when(locationService.removeDepartmentFromLocation(location.getId(), mechanic.getId())).thenReturn(location);

        mockMvc.perform(delete("/api/v1/locations/{id}/departments/{departmentId}/remove",
                location.getId(),
                department.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldFailToRemoveDepartmentFromLocation_NotFound() throws Exception {
        when(locationService.removeDepartmentFromLocation(location.getId(), mechanic.getId()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(delete("/api/v1/locations/{id}/departments/{departmentId}/remove",
                location.getId(),
                department.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404));
    }

    @Test
    void shouldGetMechanicsOnLocation() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);
        when(mechanicService.getEmployee(mechanic.getId())).thenReturn(mechanic);
        when(locationService.assignMechanic(location.getId(), mechanic.getId())).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/{id}/mechanics", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldGetMechanicsOnLocation_EmptyList() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/{id}/mechanics", location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    void shouldAddMechanicToLocation() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);
        when(mechanicService.getEmployee(mechanic.getId())).thenReturn(mechanic);
        when(locationService.assignMechanic(location.getId(), mechanic.getId())).thenReturn(location);

        mockMvc.perform(post("/api/v1/locations/{id}/mechanics/{mechanicId}/add",
                location.getId(),
                mechanic.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldFailToAddMechanicToLocation_NotFound() throws Exception {
        when(locationService.assignMechanic(location.getId(), mechanic.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(post("/api/v1/locations/{id}/mechanics/{mechanicId}/add",
                location.getId(),
                mechanic.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404));
    }

    @Test
    void shouldRemoveMechanicFromLocation() throws Exception {
        when(locationService.getLocation(location.getId())).thenReturn(location);
        when(mechanicService.getEmployee(mechanic.getId())).thenReturn(mechanic);
        when(locationService.assignMechanic(location.getId(), mechanic.getId())).thenReturn(location);
        when(locationService.removeMechanic(location.getId(), mechanic.getId())).thenReturn(location);

        mockMvc.perform(delete("/api/v1/locations/{id}/mechanics/{mechanicId}/remove",
                location.getId(),
                mechanic.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200));
    }

    @Test
    void shouldFailToRemoveMechanicFromLocation_NotFound() throws Exception {
        when(locationService.removeMechanic(location.getId(), mechanic.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(delete("/api/v1/locations/{id}/mechanics/{mechanicId}/remove",
                location.getId(),
                mechanic.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404));
    }
}
