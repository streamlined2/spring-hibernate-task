package com.streamlined.springhibernatetask.parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.streamlined.springhibernatetask.entity.EntityType;
import com.streamlined.springhibernatetask.validator.Validator;

import exception.ParseException;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class Parser {

    private final CsvMapper csvMapper;

    public Parser(CsvMapper csvMapper) {
        this.csvMapper = csvMapper;
    }

    public <K, T extends EntityType<K>> Map<K, T> parse(Class<T> entityClass, String sourceFileName,
            Validator<T> validator) {
        
        Map<K, T> entityMap = new HashMap<>();
        try (InputStream is = getClass().getResourceAsStream(sourceFileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            
            CsvSchema schema = CsvSchema.emptySchema().withHeader();            
            MappingIterator<T> iterator = csvMapper.readerFor(entityClass).with(schema).readValues(reader);
            for (int lineNo = 1; iterator.hasNext(); lineNo++) {
                T entity = iterator.next();
                if (validator.isValid(entity)) {
                    entityMap.put(entity.getPrimaryKey(), entity);
                } else {
                    LOGGER.info("Line number {} in file {} contains invalid entity data", lineNo,
                            sourceFileName);
                }
            }
            return entityMap;
        } catch (Exception e) {
            LOGGER.debug("Cannot parse input data", e);
            throw new ParseException("Cannot parse input data", e);
        }
    }

}
