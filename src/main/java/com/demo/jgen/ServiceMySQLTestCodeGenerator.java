package com.demo.jgen;

import io.swagger.v3.oas.models.media.Schema;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceMySQLTestCodeGenerator implements BaseCodeGenerator {
    private final AtomicInteger counter = new AtomicInteger(1); // 순서를 위한
    private final Random random = new Random();

    @Override
    public void generateCode(String packageName, String resourceName, String packageDir, Schema schema) throws IOException {
        // Extracting schema properties (fields)
        Map<String, Schema> properties = schema.getProperties();

        // Generate `setUp` code for two DTOs
        StringBuilder setUpCode = new StringBuilder();
        StringBuilder testCreateSetupCode = new StringBuilder();
        StringBuilder testUpdateSetupCode = new StringBuilder();
        setUpCode.append("// 생성된 첫 번째 DTO\n");
        setUpCode.append("%sDto dto1 = new %sDto();\n".formatted(resourceName, resourceName));
        //return String.format("dto.set%s(%dL);\n", capitalize(fieldName), random.nextLong(10000));
        int idx = 0;
        for (Map.Entry<String, Schema> entry : properties.entrySet()) {
            String fieldName = entry.getKey();
            Schema fieldSchema = entry.getValue();
            String fieldType = mapSchemaTypeToJavaType(fieldSchema.getType(), fieldSchema.getFormat());
            setUpCode.append(generateFieldInitializationCode(fieldName, fieldType).replace("dto", "dto1"));
            if(idx==0){
                testCreateSetupCode.append(String.format("dto.setId(%dL);\n", random.nextLong(10000)));
            }
            testCreateSetupCode.append(generateFieldInitializationCode(fieldName, fieldType));
            testUpdateSetupCode.append(generateFieldInitializationCodeForUpdate(fieldName, fieldType));
            idx++;
        }
        setUpCode.append("dto1Id = dto1.getId();");

        setUpCode.append("\n// 생성된 두 번째 DTO\n");
        setUpCode.append("%sDto dto2 = new %sDto();\n".formatted(resourceName, resourceName));
        for (Map.Entry<String, Schema> entry : properties.entrySet()) {
            String fieldName = entry.getKey();
            Schema fieldSchema = entry.getValue();
            String fieldType = mapSchemaTypeToJavaType(fieldSchema.getType(), fieldSchema.getFormat());
            setUpCode.append(generateFieldInitializationCode(fieldName, fieldType).replace("dto", "dto2"));
        }
        setUpCode.append("dto2Id = dto2.getId();");
        setUpCode.append("\nmapper.insert(dto1);\n");
        setUpCode.append("mapper.insert(dto2);\n");

        // Generate the full test class code
        String serviceMySQLTestCode = """
            package %s;

            import static org.assertj.core.api.Assertions.assertThat;

            import org.junit.jupiter.api.BeforeEach;
            import org.junit.jupiter.api.Test;
            import org.springframework.beans.factory.annotation.Autowired;
            import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
            import org.springframework.test.context.jdbc.Sql;
            import org.springframework.transaction.annotation.Transactional;

            import java.util.List;

            @MybatisTest
            @Sql(scripts = "/%sTest-data.sql")  // 테스트 데이터 로딩 (파일 경로는 환경에 맞게 조정)
            @Transactional
            public class %sServiceMySQLTest {

                @Autowired
                private %sMapper mapper;

                @BeforeEach
                public void setUp() {
                    // 테스트 전에 실행되는 초기화 코드
                }

                @Test
                public void testFindAll() {
                    // Act
                    List<%sDto> result = mapper.findAll();

                    // Assert
                    assertThat(result).isNotNull();
                }

                @Test
                public void testFindByPage() {
                    // Arrange
                    int page = 1;
                    int size = 10;

                    // Act
                    List<%sDto> result = mapper.findByPage(page, size);

                    // Assert
                    assertThat(result).isNotNull();
                }

                @Test
                public void testFindById() {
                    // Arrange
                    Long mockId = 1L;

                    // Act
                    %sDto result = mapper.findById(mockId);

                    // Assert
                    assertThat(result).isNotNull();
                    assertThat(mockId).isEqualTo(result.getId());
                }

                @Test
                public void testInsert() {
                    // Arrange
                    %sDto dto = new %sDto();
                    %s

                    // Act
                    mapper.insert(dto);

                    // Assert
                    assertThat(mapper.findById(dto.getId())).isNotNull();
                }

                @Test
                public void testUpdate() {
                    // Arrange
                    Long mockId = 1L;
                    %sDto dto = mapper.findById(mockId);
            %s

                    // Act
                    mapper.update(dto);

                    // Assert
                    %sDto updated = mapper.findById(mockId);
                    assertThat(updated).isNotNull();
                    //assertThat(dto.getName()).isEqualTo(updated.getName());   예시.바뀐값 확인하기
                }

                @Test
                public void testDeleteById() {

                    Long mockId = 1L;

                    // Act
                    mapper.deleteById(mockId);

                    // Assert
                    assertThat(mapper.findById(mockId)).isNull();
                }
            }
            """.formatted(
                packageName,        //패키지 package %s
                resourceName,     //@SQL()
                resourceName, resourceName,     //Class 이름, Mapper 이름
                resourceName,   //testFindAll()
                resourceName,   //testFindByPage()
                resourceName,   //tesfFindById()
                resourceName, resourceName, testCreateSetupCode.toString(),   //testInsert()
                resourceName, testUpdateSetupCode.toString(), resourceName  //testUpdate()
        );

        // Write the generated code to a file
        writeToFile(packageDir + resourceName + "ServiceMySQLTest.java", serviceMySQLTestCode);
        System.out.println("DataBase Test Code generated");
    }

    private String mapSchemaTypeToJavaType(String schemaType, String schemaFormat) {
        if (schemaType == null) {
            return "Object";
        }

        switch (schemaType) {
            case "string":
                if ("date".equals(schemaFormat)) {
                    return "java.time.LocalDate";
                } else if ("date-time".equals(schemaFormat)) {
                    return "java.time.LocalDateTime";
                } else if ("uuid".equals(schemaFormat)) {
                    return "java.util.UUID";
                } else {
                    return "String";
                }

            case "integer":
                if ("int32".equals(schemaFormat)) {
                    return "Integer";
                } else if ("int64".equals(schemaFormat)) {
                    return "Long";
                } else {
                    return "Integer"; // Default to Integer for unspecified formats
                }

            case "number":
                if ("float".equals(schemaFormat)) {
                    return "Float";
                } else if ("double".equals(schemaFormat)) {
                    return "Double";
                } else {
                    return "Double"; // Default to Double for unspecified formats
                }

            case "boolean":
                return "Boolean";

            case "array":
                return "List"; // Could be extended to include generics if items are provided

            case "object":
                return "Map"; // Could be extended for specific object definitions

            default:
                return "Object"; // Fallback for unknown types
        }
    }

    private String generateFieldInitializationCode(String fieldName, String fieldType) {
        int uniqueId = counter.getAndIncrement();

        // Handle the 'id' field separately to always set it as Long
        if ("id".equals(fieldName)) {
//            return String.format("dto.set%s(%dL);\n", capitalize(fieldName), random.nextLong(10000)); // Long type for 'id'
            return "";
        }

//        // If there is no 'id' field in the DTO, make sure to add the setId(random)
//        if ("id".equalsIgnoreCase(fieldName)) {
//            return String.format("dto.setId(%dL);\n", random.nextLong(10000)); // Set a random id if it doesn't exist
//        }

        switch (fieldType) {
            case "String":
                return String.format("        dto.set%s(\"%s_value_%d\");\n", capitalize(fieldName), fieldName, uniqueId);
            case "Integer":
                return String.format("        dto.set%s(%d);\n", capitalize(fieldName), random.nextInt(1000));
            case "Long":
                return String.format("        dto.set%s(%dL);\n", capitalize(fieldName), random.nextLong(10000));
            case "Double":
                return String.format("        dto.set%s(%.2f);\n", capitalize(fieldName), random.nextDouble() * 100);
            case "Boolean":
                return String.format("        dto.set%s(%s);\n", capitalize(fieldName), uniqueId % 2 == 0 ? "true" : "false");
            case "List":
                return String.format("        dto.set%s(List.of(\"item_%d\", \"item_%d\"));\n", capitalize(fieldName), uniqueId, uniqueId + 1);
            case "Map":
                return String.format("        dto.set%s(Map.of(\"key_%d\", \"value_%d\", \"key_%d\", \"value_%d\"));\n",
                        capitalize(fieldName), uniqueId, uniqueId, uniqueId + 1, uniqueId + 1);
            default:
                return String.format("        dto.set%s(\"default_value_%d\");\n", capitalize(fieldName), uniqueId);
        }
    }

    // Modified to avoid setting the 'id' field in the update test
    private String generateFieldInitializationCodeForUpdate(String fieldName, String fieldType) {
        int uniqueId = counter.getAndIncrement();

        // Skip setting the 'id' field for update test
        if ("id".equals(fieldName)) {
            return "";  // Do not include the id field for update
        }

        switch (fieldType) {
            case "String":
                return String.format("        dto.set%s(\"updated_%s_value_%d\");\n", capitalize(fieldName), fieldName, uniqueId);
            case "Integer":
                return String.format("        dto.set%s(%d);\n", capitalize(fieldName), random.nextInt(1000));
            case "Long":
                return String.format("        dto.set%s(%dL);\n", capitalize(fieldName), random.nextLong(10000));
            case "Double":
                return String.format("        dto.set%s(%.2f);\n", capitalize(fieldName), random.nextDouble() * 100);
            case "Boolean":
                return String.format("        dto.set%s(%s);\n", capitalize(fieldName), uniqueId % 2 == 0 ? "true" : "false");
            case "List":
                return String.format("        dto.set%s(List.of(\"item_%d\", \"item_%d\"));\n", capitalize(fieldName), uniqueId, uniqueId + 1);
            case "Map":
                return String.format("        dto.set%s(Map.of(\"key_%d\", \"value_%d\", \"key_%d\", \"value_%d\"));\n",
                        capitalize(fieldName), uniqueId, uniqueId, uniqueId + 1, uniqueId + 1);
            default:
                return String.format("        dto.set%s(\"updated_default_value_%d\");\n", capitalize(fieldName), uniqueId);
        }
    }

    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }


}
