package com.briup.cms;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.briup.cms.common.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * EasyExcel整体使用思路：
 * 与POI有一点相同，都是面向对象操作。
 * POI：把文档结构映射成对象（把文档、工作表、行、列、样式都映射成对象）
 * EasyExcel：把表中数据映射成对象（类似MyBatis中ORM思想）
 * 如果有对行、列、样式进行的设置，大多通过在Entity属性上使用注解声明
 * <p>
 * 1、调用EasyExcel类的静态方法read()/write()开始读或写模式
 * 这两个方法都会返回一个Builder，用来对读写的方式进行配置
 * 2、EasyExcel读取Excel文档信息的两种整体思路：
 * 1）过程式
 * 需要提供一个监听器，对框架中的组件读取Excel中的每一行信息行为进行监听，
 * 读到Excel中的每一行之后，都会调用监听器中的回调方法进行处理。
 * 需要定义监听器类型，并且使用Builder进行注册。
 * 2）结果式
 *
 * @author YuYan
 * @date 2023-12-11 14:03:36
 */
public class EasyExcelTest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Student {
        @ExcelProperty(index = 3, value = "性别")
        private String gender;
        @ExcelProperty(index = 2, value = "年龄")
        private Integer age;
        @ExcelProperty(index = 1, value = "姓名")
        private String name;
        @ExcelProperty(index = 0, value = "学号")
        private Integer id;
    }

    static class MyReadListener extends AnalysisEventListener<Student> {
        /**
         * 当读到每一行数据时调用
         * 读到表中每一行数据，都会自动解析并映射成为一个Student对象。
         * 如果没有任何特殊的配置，框架会按照表中字段从左到右的顺序，
         * 依次映射实体类中从上到下顺序的属性。
         * 通常我们都会在实体属性上添加一些注解，用来指定属性和Excel中字段的映射关系。
         * 使用一个@ExcelProperty注解进行配置
         * 1）通过索引（顺序）配置：index属性
         * 不建议通过索引映射字段，了解即可。
         * 2）通过名称配置：value属性
         *
         * @param data    读到的一行数据封装成Entity的结果
         * @param context
         */
        @Override
        public void invoke(Student data, AnalysisContext context) {
            System.out.println(data);
            /**
             * 如果要读取的数据量非常庞大，建议使用监听器式的事件驱动读取方法
             * 在实际开发中，invoke()方法这里要做的事情就是调用Service/Dao层组件，
             * 将EasyExcel为我们封装好的Entity对象保存入库即可。
             */
        }

        /**
         * 当所有的数据读取完毕之后调用一次
         *
         * @param context
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            System.out.println("所有数据读取完毕！");
        }

        /**
         * 当读取过程出现异常时调用
         *
         * @param exception
         * @param context
         * @throws Exception
         */
        @Override
        public void onException(Exception exception, AnalysisContext context) throws Exception {
            System.out.println("出现了异常：" + exception.getMessage());
        }
    }

    @Test
    void read1() throws Exception {
        /* 1、获取到Excel读取器的Builder对象 */
        EasyExcel.read()
                /* 2、指定要读取的文件 */
                .file("/Users/yuyan/Desktop/students.xlsx")
                /* 3、使用head()方法指定与表格对应的实体类型 */
                .head(Student.class)
                /* 4、注册监听器*/
                .registerReadListener(new MyReadListener())
                /* 5、获取工作表对象 */
                .sheet("Sheet1")
                /* 6、工作表对象开始执行读取操作 */
                .doRead();
    }


    @Test
    void read2() throws Exception {
        /* 1、获取到Excel读取器的Builder对象 */
        List<Student> students = EasyExcel.read()
                /* 2、指定要读取的文件 */
                .file("/Users/yuyan/Desktop/students.xlsx")
                /* 3、使用head()方法指定与表格对应的实体类型 */
                .head(Student.class)
                /* 4、调用同步读取全部数据的方法 */
                /* 1）会读到所有数据一并返回 */
                /* 2）同步方式读取，如果没读完不会返回 */
                /* 3）返回值是一个List<Entity>集合 */
                .doReadAllSync();
        /* 打印输出读到的结果 */
        students.forEach(System.out::println);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Student2 {
        @ExcelProperty(value = "性别")
        private Integer gender;
        @ExcelProperty(value = "年龄")
        private Integer age;
        @ExcelProperty(value = "姓名")
        private String name;
        @ExcelProperty(value = "学号")
        private Integer id;
    }

    /**
     * 自定义转换器，当框架组件读取到一行数据并准备自动封装实体对象的时候，
     * 如果发现出现了目标类型属性，就会调用该转换器中的方法自动进行处理。
     * 实现接口的时候需要传递泛型参数：Java中该实体属性类型
     */
    public static class MyGenderConvertor implements Converter<Integer> {
        /**
         * 返回Java实体中的属性类型
         *
         * @return
         */
        @Override
        public Class<?> supportJavaTypeKey() {
            /* gender属性是一个Integer字段，所以这里返回Integer类型 */
            return Integer.class;
        }

        /**
         * Excel文档中该字段的类型
         *
         * @return
         */
        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }

        /**
         * 用于从Excel中读取数据，将读取到的某个字段转换为Java实体需要的属性类型
         *
         * @param cellData            读到的一个单元格数据
         * @param contentProperty
         * @param globalConfiguration
         * @return
         * @throws Exception
         */
        @Override
        public Integer convertToJavaData(ReadCellData<?> cellData,
                                         ExcelContentProperty contentProperty,
                                         GlobalConfiguration globalConfiguration) throws Exception {
            // System.out.println("cellData.getStringValue() = " + cellData.getStringValue());
            // 如果读到male就返回1，如果读到female就返回0
            return ObjectUtil.equals(cellData.getStringValue(), "male") ? 1 :
                    ObjectUtil.equals(cellData.getStringValue(), "female") ? 0 : null;
        }

        /**
         * 用于写入Excel文件，将Java中的属性转换成Excel需要的字段类型
         *
         * @param value
         * @param contentProperty
         * @param globalConfiguration
         * @return
         * @throws Exception
         */
        @Override
        public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
            /* 如果读到的Integer类型属性不是gender字段则正常封装为装有原值的WriteCellData对象 */
            if (!contentProperty.getField().getName().equals("gender")) {
                return new WriteCellData<Integer>(String.valueOf(value));
            }
            return new WriteCellData<Integer>(value.equals(0) ? "女" : value.equals(1) ? "男" : "未知");
        }

        // @Override
        // public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        //     System.out.println("convertToExcelData2()...");
        //     return new WriteCellData<Integer>("string");
        // }
    }

    /**
     * 处理表中字段类型和Java实体属性类型不一致的情况
     * Excel：文字男或女表示性别
     * Entity：性别字段，使用0表示男，1表示女
     *
     * @throws Exception
     */
    @Test
    void read3() throws Exception {
        /* 1、获取到Excel读取器的Builder对象 */
        List<Student2> students = EasyExcel.read()
                /* 2、指定要读取的文件 */
                .file("/Users/yuyan/Desktop/students.xlsx")
                /* 3、使用head()方法指定与表格对应的实体类型 */
                .head(Student2.class)
                /* 4、注册类型转换需要使用的转换器 */
                .registerConverter(new MyGenderConvertor())
                /* 5、调用同步读取全部数据的方法 */
                /* 1）会读到所有数据一并返回 */
                /* 2）同步方式读取，如果没读完不会返回 */
                /* 3）返回值是一个List<Entity>集合 */
                .doReadAllSync();
        /* 打印输出读到的结果 */
        students.forEach(System.out::println);
    }

    @Test
    void write() throws Exception {
        List<Student2> list = Arrays.asList(
                new Student2(1, 22, "张三", 1001),
                new Student2(0, 23, "李四", 1002),
                new Student2(1, 24, "王五", 1003),
                new Student2(0, 25, "赵六", 1004)
        );

        ExcelWriterBuilder builder = EasyExcel.write();
        /* 指定要输出的文件 */
        ExcelWriter writer = builder.file(new FileOutputStream(
                "/Users/yuyan/Desktop/students3.xlsx"))
                /* 指定与表格对应的实体类 */
                .head(Student2.class)
                /* 指定要输出的文件类型 */
                .excelType(ExcelTypeEnum.XLSX)
                /* 注册转换器 */
                .registerConverter(new MyGenderConvertor())
                .build();
        /* 调用write()方法，写入数据即可 */
        // writer.write(list, builder.sheet(0).build());
        WriteSheet sheet = EasyExcel
                .writerSheet("测试工作表1")
                .build();
        writer.write(list, sheet);
        writer.finish();
        writer.close();

    }

}
