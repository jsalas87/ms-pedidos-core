package com.rfsc.pedidos.adapter.rest;


import com.rfsc.pedidos.adapter.rest.exception.SqlReaderException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@UtilityClass
@Slf4j
public class SqlReader {

    public String readSql(final String sqlPath) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(sqlPath);
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new InputStreamReader(resource.getInputStream(),
                StandardCharsets.UTF_8.name())) {
            textBuilder.append(FileCopyUtils.copyToString(reader));
            return textBuilder.toString().replace("\n", " ");
        } catch (IOException ex) {
            log.error("Error al leer el archivo sql", ex);
            throw new SqlReaderException(ex);
        }
    }
}
