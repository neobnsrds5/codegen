//package com.demo.jgen;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.media.Schema;
//import io.swagger.v3.parser.OpenAPIV3Parser;
//
//import javax.swing.*;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Map;
//
//public class OpenApiCodeGenerator {
//
//	private static final String PACKAGEPRIFIX = "com.example.demo";
//
//    public static void main(String[] args) {
//        try {
////            String yamlFilePath = System.getProperty("yamlFilePath", "./src/main/resources/api-docs.yml");
////            String outputDir = System.getProperty("outputDir", "./Customer/");
//            String yamlFilePath = System.getProperty("yamlFilePath", "./src/main/resources/api-docs.yml");
//            String outputDir = "C:/Users/NEOBNS/Documents/neo/neobns/SpiderDemoApplication/"; // 절대 경로 설정
//            File targetDir = new File(outputDir);
//            System.out.println("Generating code to " + targetDir.getAbsolutePath());
//			if (!targetDir.exists()) {
//				targetDir.mkdirs();
//			}
//
//            OpenAPI openAPI = new OpenAPIV3Parser().read(new File(yamlFilePath).getAbsolutePath());
//
//            String packageName = PACKAGEPRIFIX + ((PACKAGEPRIFIX.endsWith("."))? "":".") + "config";
//            String packageDir = outputDir + packageName.replace(".", "/") + "/";
//            new File(packageDir).mkdirs();
//            BaseCodeGenerator dbConfigGenerator = new DbConfigGenerator();
//            dbConfigGenerator.generateCode(packageName , "resourceName", packageDir, new Schema() );
//
//            String schedulePackageName = PACKAGEPRIFIX + ((PACKAGEPRIFIX.endsWith("."))? "":".") + "schedule";
//            String schedulePackageDir = outputDir + schedulePackageName.replace(".", "/") + "/";
//            new File(schedulePackageDir).mkdirs();
//            BaseCodeGenerator scheduleGenerator = new ScheduleGenerator();
//            scheduleGenerator.generateCode(schedulePackageName , "", schedulePackageDir, new Schema() );
//
//            generateCode(openAPI, outputDir);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void generateCode(OpenAPI openAPI, String outputDir) throws IOException {
//        Map<String, Schema> schemas = openAPI.getComponents().getSchemas();
//
//        for (String schemaName : schemas.keySet()) {
//            String resourceName = capitalize(schemaName);
//            String packageName = PACKAGEPRIFIX + ((PACKAGEPRIFIX.endsWith("."))? "":".") + resourceName.toLowerCase();
//            String packageDir = outputDir + packageName.replace(".", "/") + "/";
//
//            new File(packageDir).mkdirs();
//
//            BaseCodeGenerator dtoGenerator = new DTOCodeGenerator();
//            BaseCodeGenerator mapperGenerator = new MapperCodeGenerator();
//            BaseCodeGenerator mapperXmlGenerator = new MapperXmlCodeGenerator();
//            BaseCodeGenerator serviceGenerator = new ServiceCodeGenerator();
//            BaseCodeGenerator controllerGenerator = new ControllerCodeGenerator();
////            BaseCodeGenerator controllerTestGenerator = new ControllerMockTestCodeGenerator();
//            BaseCodeGenerator controllerBootTestGenerator = new ControllerBootTestCodeGenerator();
//            BaseCodeGenerator serviceBootTestCodeGenerator = new ServiceBootTestCodeGenerator();
//            BaseCodeGenerator serviceTestGenerator = new ServiceTestCodeGenerator();
//            BaseCodeGenerator serviceMySQLCodeGenerator = new ServiceMySQLTestCodeGenerator();
//            BaseCodeGenerator jmeterTestPlanGenerator = new JMeterTestPlanCodeGenerator();
//            BaseCodeGenerator testDataSQLGenerator = new TestDataSQLGenerator();
//            BaseCodeGenerator asciiDocGenerator = new AsciiDocCodeGenerator();
//
//            BaseCodeGenerator dbToDbGenerator = new DbToDbGenerator();
//            BaseCodeGenerator fileToDbGenerator = new FileToDbGenerator();
//
//            dbToDbGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            fileToDbGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//
//            dtoGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            mapperGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            mapperXmlGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            serviceGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            controllerGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
////            controllerTestGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            controllerBootTestGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            serviceBootTestCodeGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            serviceTestGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            serviceMySQLCodeGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            jmeterTestPlanGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            testDataSQLGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//            asciiDocGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
//
////            AsciiDocCodeGenerator asciiDocGenerator = new AsciiDocCodeGenerator();
////            asciiDocGenerator.generateAsciiDoc(packageDir);  // Generate AsciiDoc in the resource-specific directory
//            showFileContentInDialog(packageDir + resourceName + "Dto.java");
//            showFileContentInDialog(packageDir + resourceName + "Mapper.java");
//            showFileContentInDialog(packageDir + resourceName + "Service.java");
//        }
//    }
//
//    private static void showFileContentInDialog(String filePath) {
//        try {
//            File file = new File(filePath);
//            if (file.exists()) {
//                // Read the content of the file
//                StringBuilder content = new StringBuilder();
//                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        content.append(line).append("\n");
//                    }
//                }
//                // Show the file content in a message dialog
//                JOptionPane.showMessageDialog(null, content.toString(), "File Content: " + file.getName(), JOptionPane.INFORMATION_MESSAGE);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String capitalize(String str) {
//        if (str == null || str.isEmpty()) {
//            return str;
//        }
//        return str.substring(0, 1).toUpperCase() + str.substring(1);
//    }
//
//
//    public static String mapSchemaTypeToJavaType(String schemaType, String schemaFormat) {
//        if (schemaType == null) {
//            return "Object";
//        }
//
//        switch (schemaType) {
//            case "string":
//                if ("date".equals(schemaFormat)) {
//                    return "java.time.LocalDate";
//                } else if ("date-time".equals(schemaFormat)) {
//                    return "java.time.LocalDateTime";
//                } else if ("uuid".equals(schemaFormat)) {
//                    return "java.util.UUID";
//                } else {
//                    return "String";
//                }
//
//            case "integer":
//                if ("int32".equals(schemaFormat)) {
//                    return "Integer";
//                } else if ("int64".equals(schemaFormat)) {
//                    return "Long";
//                } else {
//                    return "Integer"; // Default to Integer for unspecified formats
//                }
//
//            case "number":
//                if ("float".equals(schemaFormat)) {
//                    return "Float";
//                } else if ("double".equals(schemaFormat)) {
//                    return "Double";
//                } else {
//                    return "Double"; // Default to Double for unspecified formats
//                }
//
//            case "boolean":
//                return "Boolean";
//
//            case "array":
//                return "List"; // Could be extended to include generics if items are provided
//
//            case "object":
//                return "Map"; // Could be extended for specific object definitions
//
//            default:
//                return "Object"; // Fallback for unknown types
//        }
//    }
//
//}

package com.demo.jgen;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class OpenApiCodeGenerator {

    private static final String PACKAGE_PREFIX = "com.example.demo";

    public static void main(String[] args) {
        try {
//            String yamlFilePath = System.getProperty("yamlFilePath", "./src/main/resources/api-docs.yml");
            String yamlFilePath = "C:/Users/NEOBNS/Documents/banking-api.yml";
            String baseDir = "C:\\Users\\NEOBNS\\Documents\\neo\\neobns\\SpiderDemoApplication";
            String generatedDir = baseDir + File.separator + "Generated";
            String batchDir = generatedDir + File.separator + "sampleCode";

            File targetDir = new File(batchDir);
            System.out.println("Generating code to " + targetDir.getAbsolutePath());
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            OpenAPI openAPI = new OpenAPIV3Parser().read(new File(yamlFilePath).getAbsolutePath());



            generateCode(openAPI, batchDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateCode(OpenAPI openAPI, String outputDir) throws IOException {
        Map<String, Schema> schemas = openAPI.getComponents().getSchemas();
        String configPackageName = PACKAGE_PREFIX + ((PACKAGE_PREFIX.endsWith(".")) ? "" : ".") + "batch_db_config";
        String configPackageDir = outputDir + configPackageName.replace(".", "/") + "/";
        new File(configPackageDir).mkdirs();
        BaseCodeGenerator dbConfigGenerator = new DbConfigGenerator();
        dbConfigGenerator.generateCode(configPackageName, "resourceName", configPackageDir, new Schema());

        String schedulePackageName = PACKAGE_PREFIX + ((PACKAGE_PREFIX.endsWith(".")) ? "" : ".") + "batch_schedule";
        String schedulePackageDir = outputDir + schedulePackageName.replace(".", "/") + "/";
        new File(schedulePackageDir).mkdirs();
        BaseCodeGenerator scheduleGenerator = new ScheduleGenerator();
        scheduleGenerator.generateCode(schedulePackageName, "", schedulePackageDir, new Schema());
        for (String schemaName : schemas.keySet()) {
            String resourceName = capitalize(schemaName);
            String packageName = PACKAGE_PREFIX + ((PACKAGE_PREFIX.endsWith(".")) ? "" : ".") + resourceName.toLowerCase();
            String packageDir = outputDir + packageName.replace(".", "/") + "/";

            new File(packageDir).mkdirs();



            BaseCodeGenerator dtoGenerator = new DTOCodeGenerator();
            BaseCodeGenerator mapperGenerator = new MapperCodeGenerator();
            BaseCodeGenerator mapperXmlGenerator = new MapperXmlCodeGenerator();
            BaseCodeGenerator serviceGenerator = new ServiceCodeGenerator();
            BaseCodeGenerator controllerGenerator = new ControllerCodeGenerator();
            BaseCodeGenerator controllerBootTestGenerator = new ControllerBootTestCodeGenerator();
            BaseCodeGenerator serviceBootTestCodeGenerator = new ServiceBootTestCodeGenerator();
            BaseCodeGenerator serviceTestGenerator = new ServiceTestCodeGenerator();
            BaseCodeGenerator serviceMySQLCodeGenerator = new ServiceMySQLTestCodeGenerator();
            BaseCodeGenerator jmeterTestPlanGenerator = new JMeterTestPlanCodeGenerator();
            BaseCodeGenerator testDataSQLGenerator = new TestDataSQLGenerator();
            BaseCodeGenerator asciiDocGenerator = new AsciiDocCodeGenerator();


            BaseCodeGenerator dbToDbGenerator = new DbToDbGenerator();
            BaseCodeGenerator fileToDbGenerator = new FileToDbGenerator();


            new File(packageDir + "batch/").mkdirs();
            dbToDbGenerator.generateCode(packageName, resourceName, packageDir + "batch/", schemas.get(schemaName));
            fileToDbGenerator.generateCode(packageName, resourceName, packageDir + "batch/", schemas.get(schemaName));
            new File(packageDir + "dto/").mkdirs();
            dtoGenerator.generateCode(packageName, resourceName, packageDir + "dto/", schemas.get(schemaName));
            new File(packageDir + "mapper/").mkdirs();
            mapperGenerator.generateCode(packageName, resourceName, packageDir + "mapper/", schemas.get(schemaName));
            new File(packageDir + "resources/").mkdirs();
            mapperXmlGenerator.generateCode(packageName, resourceName, packageDir + "resources/", schemas.get(schemaName));
            new File(packageDir + "service/").mkdirs();
            serviceGenerator.generateCode(packageName, resourceName, packageDir + "service/", schemas.get(schemaName));
            new File(packageDir + "controller/").mkdirs();
            controllerGenerator.generateCode(packageName, resourceName, packageDir + "controller/", schemas.get(schemaName));
            new File(packageDir + "test/").mkdirs();
            controllerBootTestGenerator.generateCode(packageName, resourceName, packageDir + "test/", schemas.get(schemaName));
            new File(packageDir + "test/").mkdirs();
            serviceBootTestCodeGenerator.generateCode(packageName, resourceName, packageDir + "test/", schemas.get(schemaName));
            new File(packageDir + "test/").mkdirs();
            serviceTestGenerator.generateCode(packageName, resourceName, packageDir + "test/", schemas.get(schemaName));
            new File(packageDir + "test/").mkdirs();
            serviceMySQLCodeGenerator.generateCode(packageName, resourceName, packageDir + "test/", schemas.get(schemaName));
            jmeterTestPlanGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));
            new File(packageDir + "resources/").mkdirs();
            testDataSQLGenerator.generateCode(packageName, resourceName, packageDir + "resources/", schemas.get(schemaName));
            asciiDocGenerator.generateCode(packageName, resourceName, packageDir, schemas.get(schemaName));

//            showFileContentInDialog(packageDir + resourceName + "Dto.java");
//            showFileContentInDialog(packageDir + resourceName + "Mapper.java");
//            showFileContentInDialog(packageDir + resourceName + "Service.java");
        }
    }

    private static void showFileContentInDialog(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                StringBuilder content = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(null, content.toString(), "File Content: " + file.getName(), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String mapSchemaTypeToJavaType(String schemaType, String schemaFormat) {
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
                    return "Integer";
                }

            case "number":
                if ("float".equals(schemaFormat)) {
                    return "Float";
                } else if ("double".equals(schemaFormat)) {
                    return "Double";
                } else {
                    return "Double";
                }

            case "boolean":
                return "Boolean";

            case "array":
                return "List";

            case "object":
                return "Map";

            default:
                return "Object";
        }
    }
}
