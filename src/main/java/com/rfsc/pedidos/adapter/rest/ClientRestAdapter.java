package com.rfsc.pedidos.adapter.rest;

import com.rfsc.pedidos.adapter.rest.exception.BadRequestRestClientException;
import com.rfsc.pedidos.adapter.rest.exception.EmptyOrNullBodyRestClientException;
import com.rfsc.pedidos.adapter.rest.exception.NotFoundRestClientException;
import com.rfsc.pedidos.adapter.rest.exception.TimeoutRestClientException;
import com.rfsc.pedidos.adapter.rest.handler.RestTemplateErrorHandler;
import com.rfsc.pedidos.adapter.rest.model.ClientModel;
import com.rfsc.pedidos.application.port.out.ClientRepository;
import com.rfsc.pedidos.config.ErrorCode;
import com.rfsc.pedidos.config.RestProperties;
import com.rfsc.pedidos.domain.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.WebGraphQlClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
public class ClientRestAdapter implements ClientRepository {

    private static final String PATH_GET_CLIENT_QL = "graphql/get-client.graphql";

    private final RestProperties restProperties;
    private final HttpGraphQlClient graphQlClient;
    private String getClientGraphQl;

    public ClientRestAdapter(RestProperties restProperties) {
        this.restProperties = restProperties;
        this.getClientGraphQl = SqlReader.readSql(PATH_GET_CLIENT_QL);
        WebClient client = WebClient.builder()
                .baseUrl(this.restProperties.getClient())
                .build();
        this.graphQlClient = HttpGraphQlClient.builder(client).build();
    }

    @Override
    public Mono<Client> getClient(Integer clientId) {
        log.info("Client Request: {}", restProperties.getClient());
        String query = getClientGraphQl.replace(":ID", clientId.toString());

        return graphQlClient.document(query)
                .retrieve("cliente")
                .toEntity(ClientModel.class)
                .map(a-> a.toDomain(clientId))
                .doOnNext(client -> log.info("Llamado a Api Nest GraphQL clientes ha terminado, {}", client));
    }

}
