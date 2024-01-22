// package com.briup.cms;
//
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import org.apache.poi.hssf.usermodel.HSSFCell;
// import org.apache.poi.hssf.usermodel.HSSFRow;
// import org.apache.poi.hssf.usermodel.HSSFSheet;
// import org.apache.poi.hssf.usermodel.HSSFWorkbook;
// import org.apache.poi.ss.usermodel.Row;
// import org.apache.poi.ss.usermodel.Sheet;
// import org.apache.poi.xssf.streaming.SXSSFWorkbook;
// import org.junit.jupiter.api.Test;
//
// import java.io.FileInputStream;
// import java.io.FileOutputStream;
// import java.util.Arrays;
// import java.util.List;
//
// /**
//  * @author YuYan
//  * @date 2023-12-11 11:02:13
//  */
// public class POIExcelTest {
//
//     @Data
//     @AllArgsConstructor
//     static class Student {
//         private Integer id;
//         private String name;
//         private Integer age;
//         private String gender;
//     }
//     @Test
//     void write() throws Exception {
//         /* 准备好要输出的集合及Student对象 */
//         List<Student> list = Arrays.asList(
//                 new Student(1001, "tom", 22, "male"),
//                 new Student(1002, "jack", 24, "male"),
//                 new Student(1003, "lucy", 23, "female"),
//                 new Student(1004, "lily", 25, "female")
//         );
//         /* 创建一个POI文档对象 - 对应一个Excel文件 */
//         /* 旧版的xls（2003之前）格式对应HSSFWorkbook类型 */
//         // HSSFWorkbook book = new HSSFWorkbook();
//         /* 新版的xlsx（2003之后）格式对应XSSFWorkbook类型 */
//         // XSSFWorkbook book = new XSSFWorkbook();
//         /* 优化版的poi-api提供了带有内存空间交换的文档类型 SXSSFWorkbook */
//         /*
//          * 当在一个文档中创建的行超过某个指定数值之后，内存中的数据会被临时写入到用户目录下的某个临时文件中，
//          * Dao最后输出的结果的时候才会依次读出并生效
//          */
//         SXSSFWorkbook book = new SXSSFWorkbook(1024);
//         /* 创建一个新的工作表对象 - 对应Excel文档中的Sheet工作表 */
//         Sheet sheet = book.createSheet("学生信息第一页");
//         /* 创建一个行对象，表示首行（参数是行的索引） - 一个Row对应表格中的一个行 */
//         Row row = sheet.createRow(0);
//         /* 向首行中添加单元格，表示表头 - 一个Cell对象对应一个单元格 */
//         row.createCell(0).setCellValue("学号");
//         row.createCell(1).setCellValue("姓名");
//         row.createCell(2).setCellValue("年龄");
//         row.createCell(3).setCellValue("性别");
//
//         /* 遍历集合中的Student对象，将对象数据写入到下方单元格中 */
//         int index = 1;
//         for (Student student : list) {
//             /* 每个学生对应表格中的一行 */
//             Row stuRow = sheet.createRow(index++);
//             /* 在这一行中添加单元格，存储学生属性值 */
//             stuRow.createCell(0).setCellValue(student.getId());
//             stuRow.createCell(1).setCellValue(student.getName());
//             stuRow.createCell(2).setCellValue(student.getAge());
//             stuRow.createCell(3).setCellValue(student.getGender());
//         }
//
//         /* 写入数据 */
//         FileOutputStream fos = new FileOutputStream("/Users/yuyan/Desktop/students.xlsx");
//         book.write(fos);
//
//         System.out.println("输出成功！");
//     }
//
//     @Test
//     void read() throws Exception {
//         /* 创建一个HSSFWorkbook对象 - 表示整个文档 */
//         FileInputStream fis = new FileInputStream("/Users/yuyan/Desktop/students.xlsx");
//         HSSFWorkbook book = new HSSFWorkbook(fis);
//         /* 根据名称获取Sheet工作表 */
//         // book.getSheet("学生信息第一页");
//         /* 根据索引获取Sheet工作表 */
//         HSSFSheet sheet = book.getSheetAt(0);
//         /* 取出首行（表头） */
//         HSSFRow row = sheet.getRow(0);
//         /* 遍历输出首行中的所有单元格（字段名） */
//         /* 取出这一行中的第一个单元格索引和最后一个单元格索引 */
//         int begin = row.getFirstCellNum();
//         int end = row.getLastCellNum();
//         for (int i = begin; i < end; i++) {
//             HSSFCell cell = row.getCell(i);
//             System.out.print(cell.getStringCellValue() + "\t");
//         }
//         System.out.println();
//
//         /* 输出学生数据 */
//         begin = sheet.getFirstRowNum();
//         end = sheet.getLastRowNum();
//         for (int i = begin + 1; i < end; i++) {
//             /* 取出工作表中的第i行 */
//             row = sheet.getRow(i);
//             System.out.print(row.getCell(0).getNumericCellValue() + "\t");
//             System.out.print(row.getCell(1).getStringCellValue() + "\t");
//             System.out.print(row.getCell(2).getNumericCellValue() + "\t");
//             System.out.print(row.getCell(3).getStringCellValue() + "\t");
//             System.out.println();
//         }
//
//     }
//
//
//
//
//
// }
