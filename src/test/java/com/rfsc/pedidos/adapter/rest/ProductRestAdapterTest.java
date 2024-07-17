package com.rfsc.pedidos.adapter.rest;

import com.rfsc.pedidos.config.RestProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductRestAdapterTest {


    @Mock
    private RestProperties restProperties;

    @InjectMocks
    private ProductRestAdapter productRestAdapter;

    private List<Integer> productIds;
    private String productUri;

    @BeforeEach
    public void setUp() {
        productIds = Arrays.asList(1, 2, 3);
        productUri = "https://387240b3-8996-4444-b6a7-a297ef998a32.mock.pstmn.io/negocio/apigov1.0/productos?ids={ids}";

        when(restProperties.getProduct()).thenReturn(productUri);
    }

    @Test
    public void testGetProducts() {

        productRestAdapter.getProducts(productIds);

        verify(restProperties, new Times(2)).getProduct();
    }
}

