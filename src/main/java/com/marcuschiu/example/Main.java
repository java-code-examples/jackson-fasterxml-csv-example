package com.marcuschiu.example;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Temp1 temp1 = new Temp1();
        temp1.list.add(new Temp2());
        temp1.list.add(new Temp2());

        CsvMapper mapper = new CsvMapper();
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        CsvSchema schema = CsvSchema.builder()
                .addColumn("field1")
                .addColumn("field2")
                .build().withHeader();
        ObjectWriter writer = mapper.writerFor(Temp2.class).with(schema);

        // write single Temp2 object
        writer.writeValueAsString(new Temp2());

        // write list of Temp2 objects
        List<Temp2> temp2List = Arrays.asList(new Temp2(), new Temp2());
        writer.writeValues(new File("sample.csv")).writeAll(temp2List);
    }

    public MappingIterator<Temp2> read(File csvFile) throws IOException {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("firstName")
                .addColumn("lastName")
                .addColumn("ignore1")
                .addColumn("dob")
                .addColumn("ignore2")
                .addColumn("postalCode")
                .build();

        CsvMapper mapper = new CsvMapper();
        return mapper
                .readerFor(Temp2.class)
                .with(schema)
                .readValues(csvFile);
    }

    @Data
    public static class Temp1 {
        @JsonUnwrapped
        public List<Temp2> list = new ArrayList<>();
    }
    @Data
    public static class Temp2 {
        public String field1 = "field1 here";
        public String field2 = "field2 here";
        public Temp1 temp1 = new Temp1();
    }
}
