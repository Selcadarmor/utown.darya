package com.example.Utown.service.impl;

import com.example.Utown.config.S3.AwsProperties;
import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientDetailsDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.AddressMapper;
import com.example.Utown.model.Address;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import com.example.Utown.service.AddressService;
import com.example.Utown.service.CartService;
import com.example.Utown.service.RoleService;
import com.example.Utown.service.S3Service.FileInfoService;
import com.example.Utown.service.UserTypeService.ClientService;
import com.example.Utown.service.UserTypeService.ClientServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AwsProperties awsProperties;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    @Spy
    private ClientServiceImpl clientService;

    @BeforeEach
    public void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnUserWhenFound() {

        Client mockClient = new Client();
        mockClient.setId(1L);
        mockClient.setUsername("01080124852");

        when(clientRepository.findByUsername("01080124852")).thenReturn(Optional.of(mockClient));

        Client result = clientService.findByUsername("01080124852");

        assertNotNull(result);
        assertEquals("01080124852", result.getUsername());
        verify(clientRepository).findByUsername("01080124852");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(clientRepository.findByUsername("01080124853")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> clientService.findByUsername("01080124853"));
        verify(clientRepository).findByUsername("01080124853");
    }


    @Test
    void shouldReturnUserByIdWhenFound() {
        Client mockClient = new Client();
        mockClient.setId(1L);
        mockClient.setUsername("01080124852");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));

        Client result = clientService.getById(1L);
        assertNotNull(result);
        assertEquals("01080124852", result.getUsername());
        verify(clientRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clientService.getById(1L));
        verify(clientRepository).findById(1L);
    }

    @Test
    void testGetClientById_withPath() {
        ClientInfoDto dto =  new ClientInfoDto(
                "Иван Иванов",
                "01080124852",
                "Москва",
                "ул. Примерная, д.1",
                5,
                123L,
                "files/client123.jpg"
        );
        dto.setPath("files/client123.jpg");

        when(clientRepository.findClientInfoById(1L)).thenReturn(Optional.of(dto));
        when(awsProperties.getPublicBaseUrl()).thenReturn( "https://s3.amazonaws.com");
        ClientInfoDto result = clientService.getClientById(1L);
        assertEquals("files/client123.jpg", result.getPath());
        assertEquals("https://s3.amazonaws.com/files/client123.jpg", result.getFileUrl());
        assertEquals("Иван Иванов",result.getFullName());
        assertEquals("01080124852",result.getUsername());
    }

    @Test
    void testGetClientById_notFound() {
        when(clientRepository.findClientInfoById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(1L));
    }

    @Test
    void testGetClientByI_nullId() {
        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(null));
    }


    @Test
    void testGetAllClients_withPageRequest() {
        String query = "John Doe";
        Boolean isActive = true;
        Pageable pageable = PageRequest.of( 0,  5, Sort.by("name").ascending());

        ClientDetailsDto dto = new ClientDetailsDto("John Smith", "john_smith", "New York", "123 Main St", 3);
        ClientDetailsDto dto1 = new ClientDetailsDto("Joni Dep", "joni_dep", "Los Angeles", "456 Sunset Blvd", 2);
        Page<ClientDetailsDto> exeptedPage = new PageImpl<>(List.of(dto,dto1), pageable, 2);
        when(clientRepository.findAllClientDetails(anyString(),
                        any(Boolean.class),
                        any(Pageable.class))).thenReturn(exeptedPage);

        Page<ClientDetailsDto> result = clientService.getAllClients(query, isActive, pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("john_smith", result.getContent().get(0).getUsername());
        assertEquals("joni_dep", result.getContent().get(1).getUsername());
        verify(clientRepository).findAllClientDetails(query, isActive, pageable);
    }

    @Test
    void testGetAllAddresses_forClient() {
        Client client = new Client();
        client.setId(1L);
        client.setUsername("01080124852");
        doReturn(client).when(clientService).getCurrentClient();

        List<AddressDto> addresses = List.of(new AddressDto("1", 1L, "Abay", "Astana", "Manasa", "1234", 4L),
                                            new AddressDto("2", 3L, "Manasa", "Almaty", "Manasa", "1236", 1L));

        when(clientRepository.getAddressesByClient("01080124852")).thenReturn(addresses);

        List<AddressDto> result = clientService.getAddressesByClient();

        assertEquals(2, result.size());
        assertEquals("Astana", result.getFirst().getCity());
        verify(clientRepository).getAddressesByClient("01080124852");

    }

    @Test
    void testReturnEmptyAddresses_forClient() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("01080124852");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        Client client = new Client();
        client.setId(1L);
        client.setUsername("01080124852");

        when(clientRepository.findByUsername("01080124852")).thenReturn(Optional.of(client));
        when(clientRepository.getAddressesByClient("01080124852")).thenReturn(Collections.emptyList());

        List<AddressDto> result = clientService.getAddressesByClient();
        assertTrue(result.isEmpty());
        verify(clientRepository).findByUsername("01080124852");
        verify(clientRepository).getAddressesByClient("01080124852");
    }

    @Test
    void testGetCurrentClient() {
     when(authentication.isAuthenticated()).thenReturn(true);
     when(authentication.getName()).thenReturn("01080124852");

     Client client = new Client();
     client.setId(1L);
     client.setUsername("01080124852");
     client.setPassword("01080124852");
     client.setIsActive(true);
     client.setFullName("John Smith");
     client.setDefaultAddress(5L);

     when(clientRepository.findByUsername("01080124852")).thenReturn(Optional.of(client));

     Client result = clientService.getCurrentClient();
     assertEquals("01080124852", result.getUsername());
     assertEquals("John Smith", result.getFullName());
     assertTrue(result.getIsActive());
     verify(clientRepository).findByUsername("01080124852");
    }

    @Test
    void testGetCurrentClient_notFound() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("01080124852");
        when(clientRepository.findByUsername("01080124852")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> clientService.getCurrentClient());
    }

    @Test
    void testGetCurrentClient_null() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> clientService.getCurrentClient());
        verify(clientRepository, never()).findByUsername(anyString());
    }

    @Test
    void testGetCurrentClient_notAuthorized() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> clientService.getCurrentClient());
        verify(clientRepository,never()).findByUsername(anyString());
    }


    @Test
    void getFavoritesRestaurants_ofListForClient() {
        Client client = new Client();
        client.setId(1L);
        client.setUsername("01080124852");
        doReturn(client).when(clientService).getCurrentClient();

        List<RestaurantForClientDto> restaurants = List.of(new RestaurantForClientDto(1L, "Sunday", "/path/3",
                        "New Restaurant", BigDecimal.valueOf(356.6), "3", true, true,
                        false, BigDecimal.valueOf(4.5), 45),
                new RestaurantForClientDto(1L, "Sunday", "/path/3",
                        "Napa", BigDecimal.valueOf(386.6), "60", true, true,
                        false, BigDecimal.valueOf(4.5), 45));


        when(clientRepository.findFavoriteRestaurants("01080124852")).thenReturn(restaurants);
        List<RestaurantForClientDto> result = clientService.getFavoriteRestaurants();
        assertEquals(2, result.size());
        assertEquals("Sunday", result.get(0).getTitle());
    }

    @Test
    void testGetFavoriteRestaurants_restaurantISEmpty() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("01080124852");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        Client client = new Client();
        client.setId(1L);
        client.setUsername("01080124852");

        when(clientRepository.findByUsername("01080124852")).thenReturn(Optional.of(client));
        when(clientRepository.findFavoriteRestaurants("01080124852")).thenReturn(Collections.emptyList());

        List<RestaurantForClientDto> result = clientService.getFavoriteRestaurants();
        assertTrue(result.isEmpty());
        verify(clientRepository).findByUsername("01080124852");
        verify(clientRepository).findFavoriteRestaurants("01080124852");
    }


}
