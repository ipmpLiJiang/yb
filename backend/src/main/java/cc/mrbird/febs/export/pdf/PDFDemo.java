package cc.mrbird.febs.export.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import lombok.Data;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author lijiang
 * @createDate 2020/11/5
 */
public class PDFDemo {
    //    private Color black = new Color(0, 0, 0); // 黑色
//    private Color red = new Color(255, 0, 0); // 红色
//    private Color blue = new Color(0, 0, 255); // 蓝色
    BaseColor black = BaseColor.BLACK;
    BaseColor red = BaseColor.RED;
    BaseColor blue = BaseColor.BLUE;
    private int bold = Font.BOLD; // 粗体
    private int normal = Font.NORMAL; // 正常字体
    private int italic = Font.ITALIC; // 斜体
    private int boldItalic = Font.BOLDITALIC; // 粗斜体
    private float setting = 100; // 首行缩进参数

    public Document createDoc(String filename) throws Exception {
        // 新建document对象
        // 第一个参数是页面大小。接下来的参数分别是左、右、上和下页边距。
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        // 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        return document;
    }

    public Image writeImg(String imgPath) throws Exception {
        Image img = Image.getInstance(imgPath); // 控制图片大小
        img.scaleAbsolute(100, 100);
        return img;
    }

    public boolean checkFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static Paragraph convertParToChinese(String text, int fontsize, int fontStyle, BaseColor color)
            throws Exception {
        BaseFont baseFontChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(baseFontChinese, fontsize, fontStyle, color);
        Paragraph graph = new Paragraph(text, fontChinese);
        return graph;
    }

    public Chunk convertChunkByChinese(String text, int fontsize, int fontStyle, BaseColor color) throws Exception {
        BaseFont baseFontChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(baseFontChinese, fontsize, fontStyle, color);
        Chunk chunk = new Chunk(text, fontChinese);
        return chunk;
    }

    private final String fontPath = "D:\\simsun.ttc,1";

    public void writePdf(String fileName, String outWatermarkFileName, ArrayList<String> mergeAddPdfList, String watermarkName) throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        FileOutputStream out = new FileOutputStream(fileName);
        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open(); // 文档里写入
        BaseFont baseFontChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

//        BaseFont baseFontChinese = BaseFont.createFont(fontPath,BaseFont.IDENTITY_H,BaseFont.EMBEDDED);

        Font font = new Font(baseFontChinese, 11, normal, black);

        Font fontBold = new Font(baseFontChinese, 11, bold, black);
        Float contentHeight40 = 40f;
        Float contentHeight45 = 45f;
        Float contentHeight50 = 50f;
        Float contentHeight55 = 55f;
        Float contentHeight30 = 30f;
        Float contentHeight25 = 25f;
        Float contentHeight35 = 35f;
        Float contentHeight60 = 60f;
        Float contentHeight65 = 65f;
        Float contentHeight6 = 6f;

        int numColumns = 25;
        int totalWidth = 520;
        int[] setWids = new int[numColumns];
        PdfPTable table = null;

        PdfPCell cell;

        String coverHg = "___________________";
        //region 封面
        String titleCover_1 = "人事编号：__________";
        String titleCover_2 = "华中科技大学教师岗位申报表";
        String titleCover_3 = "姓        名" + coverHg;
        String titleCover_4 = "所 在 院";
        String titleCover_4_1 = "(系、所)" + coverHg;
        String titleCover_5 = "现任岗位";
        String titleCover_5_1 = "(职   务)" + coverHg;
        String titleCover_6 = "拟聘岗位";
        String titleCover_6_1 = "(职   务)" + coverHg;
        String titleCover_7 = "岗位类别" + coverHg;
        String titleCover_8 = "华中科技大学聘任委员会制";
        Font fontCover1 = new Font(baseFontChinese, 18, normal, black);
        Font fontCover2 = new Font(baseFontChinese, 25, bold, black);
        Font fontCover3 = new Font(baseFontChinese, 18, bold, black);

        document.newPage();
        numColumns = 1;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);

        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //人事编号______
        cell = new PdfPCell(new Phrase(titleCover_1, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(70);
        cell.setPaddingRight(30);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //华中科技大学教师岗位申报表
        cell = new PdfPCell(new Phrase(titleCover_2, fontCover2));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(100);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //姓        名
        cell = new PdfPCell(new Phrase(titleCover_3, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(80);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //所 在 院
        cell = new PdfPCell(new Phrase(titleCover_4, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingRight(175);
        cell.setFixedHeight(40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //(系、所)
        cell = new PdfPCell(new Phrase(titleCover_4_1, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //现任岗位
        cell = new PdfPCell(new Phrase(titleCover_5, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingTop(15);
        cell.setPaddingRight(175);
        cell.setFixedHeight(50);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //(职   务)
        cell = new PdfPCell(new Phrase(titleCover_5_1, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //拟聘岗位
        cell = new PdfPCell(new Phrase(titleCover_6, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingTop(15);
        cell.setPaddingRight(175);
        cell.setFixedHeight(50);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //(职   务)
        cell = new PdfPCell(new Phrase(titleCover_6_1, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //岗位类别
        cell = new PdfPCell(new Phrase(titleCover_7, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(90);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //华中科技大学聘任委员会制
        cell = new PdfPCell(new Phrase(titleCover_8, fontCover3));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(100);
        cell.setColspan(numColumns);
        table.addCell(cell);


        document.add(table);

        //endregion

        //region 填表说明
        String strKongGe = "   ";
        String strKongGe_1 = "      ";
        String titleExp_1 = "填  表  说  明";
        ArrayList<String> expTitleList = new ArrayList<>();
        String titleExp_2 = "1." + strKongGe + "本表一律使用A4纸双面打印，第1至6页（“个人承诺”及之前内容）由本人填写，双";
        expTitleList.add(titleExp_2);
        String titleExp_2_1 = strKongGe_1 + "面打印，单位审核。如填写内容较多，可续页。";
        expTitleList.add(titleExp_2_1);
        String titleExp_3 = "2." + strKongGe + "“人事编号”填写本人新的10位人事编号。";
        expTitleList.add(titleExp_3);
        String titleExp_4 = "3." + strKongGe + "“拟聘岗位”按申报人所选择的岗位类别所属相应岗位填写。";
        expTitleList.add(titleExp_4);
        String titleExp_5 = "4." + strKongGe + "“岗位类别”按申报人所选相应岗位类别填写，即教学科研并重岗、教学岗、科研岗、";
        expTitleList.add(titleExp_5);
        String titleExp_5_1 = strKongGe_1 + "社会服务岗。";
        expTitleList.add(titleExp_5_1);
        String titleExp_6 = "5." + strKongGe + "“担任辅导员教师班主任及考核情况”填写担任**级**班班主任（辅导员），考核合格";
        expTitleList.add(titleExp_6);
        String titleExp_6_1 = strKongGe_1 + "等。";
        expTitleList.add(titleExp_6_1);
        String titleExp_7 = "6." + strKongGe + "“主要学习及工作经历”从本科学习经历开始填写，按照时间正序填写学习、工作经";
        expTitleList.add(titleExp_7);
        String titleExp_7_1 = strKongGe_1 + "历，如有时间上的间断，请注明。";
        expTitleList.add(titleExp_7_1);
        String titleExp_8 = "7." + strKongGe + "“申报拟聘岗位理由”结合申报岗位，根据本人岗位情况如实填写(要求简洁明了，按";
        expTitleList.add(titleExp_8);
        String titleExp_8_1 = strKongGe_1 + "条目式填写)，包括满足申报条件等情况，重点突出在教学、科研、社会服务方面取得";
        expTitleList.add(titleExp_8_1);
        String titleExp_8_2 = strKongGe_1 + "的主要成果，限3000字以内。";
        expTitleList.add(titleExp_8_2);
        String titleExp_9 = "8." + strKongGe + "“课程类别”填写理论课(申请教学岗请注明是否属于“通识教育基础课”或“学科大";
        expTitleList.add(titleExp_9);
        String titleExp_9_1 = strKongGe_1 + "类基础课”)、实验课、实习、毕业设计（论文）等（下同）。";
        expTitleList.add(titleExp_9_1);
        String titleExp_10 = "9." + strKongGe + "“本科教学工作获奖”填写教学成果奖、质量奖、教学竞赛奖、教学名师奖等。";
        expTitleList.add(titleExp_10);
        String titleExp_11 = "10." + strKongGe + "“收录情况”填写发表的论文被SCI/SSCI/CSSCI/EI等收录情况，并在附件材料中提供";
        expTitleList.add(titleExp_11);
        String titleExp_11_1 = strKongGe_1 + "相应的被收录报告（下同）。";
        expTitleList.add(titleExp_11_1);
        String titleExp_12 = "11." + strKongGe + "“期刊影响因子”填写论文发表当年该期刊的影响因子，并需在附件材料中提供网页下";
        expTitleList.add(titleExp_12);
        String titleExp_12_1 = strKongGe_1 + "载的证明材料（下同）。";
        expTitleList.add(titleExp_12_1);
        String titleExp_13 = "12." + strKongGe + "“他引次数”填写该论文截止当年10月31日他引次数（一般指SCI、SSCI、CSSCI、";
        expTitleList.add(titleExp_13);
        String titleExp_13_1 = strKongGe_1 + "AHCI他引）情况，并需在附件材料中提供相应的他引次数报告（下同）。";
        expTitleList.add(titleExp_13_1);
        String titleExp_14 = "13." + strKongGe + "如有一个以上的“第一作者或通讯作者”，请注明“2个同等贡献作者之一”或“3个并";
        expTitleList.add(titleExp_14);
        String titleExp_14_1 = strKongGe_1 + "列通讯作者之一”等。";
        expTitleList.add(titleExp_14_1);
        String titleExp_15 = "14." + strKongGe + "“已毕业硕士、博士论文获奖情况”填写学位论文获国家级、省级优秀论文情况。";
        expTitleList.add(titleExp_15);
        String titleExp_16 = "15." + strKongGe + "“专利类别”填写“发明专利”、“实用新型专利”、“外观设计专利”等。";
        expTitleList.add(titleExp_16);
        String titleExp_17 = "16." + strKongGe + "“其他工作及成果”填写表中未涉及的其他工作情况。";
        expTitleList.add(titleExp_17);
        String titleExp_18 = "17." + strKongGe + "“拟聘岗位工作思路及预期目标”填写申请岗位聘任后的工作计划、思路及工作目标等，";
        expTitleList.add(titleExp_18);
        String titleExp_18_1 = strKongGe_1 + "限2000字以内。";
        expTitleList.add(titleExp_18_1);

        Font fontExpTitle = new Font(baseFontChinese, 15, bold, black);
        Font fontExpContent = new Font(baseFontChinese, 12, normal, black);
        float expPaddingLeft = 20f;
        float expPaddingRight = 10f;
        float contentHeight = 22f;
        document.newPage();
        numColumns = 1;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);

        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //填表说明
        cell = new PdfPCell(new Phrase(titleExp_1, fontExpTitle));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(contentHeight50);
        cell.setPaddingBottom(10);
        cell.setColspan(numColumns);
        table.addCell(cell);

        for (String item : expTitleList) {
            cell = new PdfPCell(new Phrase(item, fontExpContent));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(expPaddingLeft);
            cell.setPaddingRight(expPaddingRight);
            cell.setFixedHeight(contentHeight);
            cell.setColspan(numColumns);
            table.addCell(cell);
        }

        document.add(table);
        //endregion

        //region 第一页
        document.newPage();
        numColumns = 25;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        String title1_1_1 = "姓        名";
        String title1_1_2 = "性别";
        String title1_1_3 = "出生年月";

        String title1_2_1 = "现专业技术岗位";
        String title1_2_2 = "聘任\n时间";
        String title1_3_1 = "来校工作时间";
        String title1_3_2 = "现从事专业\n及专长";
        String title1_4_1 = "教师资格证编号及\n获得时间";
        String title1_5_1 = "担任辅导员教师班\n主任及考核情况";
        String title1_6_1 = "社会兼职";
        String title1_7_1 = "何时何地受\n何奖励及处分";
        String title1_8_1 = "完成上一聘期\n工作任务情况";
        String title1_9_1 = "主 要 学 习 及 工 作 经 历 （从本科开始填写，含国内进修情况按时间正序连续填写）";
        String title1_10_1 = "自何年月";
        String title1_10_2 = "至何年月";
        String title1_10_3 = "在何地、何学校、何单位任职 （或学习）";
        String title1_10_4 = "证明人";

        String value1_1_1 = "value1_1_1";
        String value1_1_2 = "value1_1_2";
        String value1_1_3 = "value1_1_3";
        String value1_2_1 = "value1_2_1";
        String value1_2_2 = "value1_2_2";
        String value1_3_1 = "value1_3_1";
        String value1_3_2 = "value1_3_2";
        String value1_4_1 = "value1_4_1";
        String value1_5_1 = "value1_5_1";
        String value1_6_1 = "value1_6_1";
        String value1_7_1 = "value1_7_1";
        String value1_8_1 = "value1_8_1";

        List<TableValue> tableValueList1 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            TableValue schoolWork = new TableValue();
            schoolWork.setField1("1994.09" + i);
            schoolWork.setField2("2001.06" + i);
            schoolWork.setField3("清华大学 攻读学士学位" + i);
            schoolWork.setField4("***" + i);
            tableValueList1.add(schoolWork);
        }

        //列一
        //姓名
        cell = new PdfPCell(new Phrase(title1_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);
        //value1_1_1
        cell = new PdfPCell(new Phrase(value1_1_1, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);
        //性别
        cell = new PdfPCell(new Phrase(title1_1_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(2);
        table.addCell(cell);
        //value1_1_2
        cell = new PdfPCell(new Phrase(value1_1_2, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(3);
        table.addCell(cell);
        //出生年月
        cell = new PdfPCell(new Phrase(title1_1_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(3);
        table.addCell(cell);
        //value1_1_3
        cell = new PdfPCell(new Phrase(value1_1_3, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);
        //照片
        cell = new PdfPCell(new Phrase("照\n片", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        cell.setRowspan(3);
        table.addCell(cell);
        //列二
        //现专业技术岗位
        cell = new PdfPCell(new Phrase(title1_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);
        //value1_2_1
        cell = new PdfPCell(new Phrase(value1_2_1, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(6);
        table.addCell(cell);
        //聘任时间
        cell = new PdfPCell(new Phrase(title1_2_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(3);
        table.addCell(cell);
        //value1_2_2
        cell = new PdfPCell(new Phrase(value1_2_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(7);
        table.addCell(cell);

        //列三
        //来校工作时间
        cell = new PdfPCell(new Phrase(title1_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);
        //value1_3_1
        cell = new PdfPCell(new Phrase(value1_3_1, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(6);
        table.addCell(cell);
        //现从事专业及专长
        cell = new PdfPCell(new Phrase(title1_3_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(3);
        table.addCell(cell);
        //value1_3_2
        cell = new PdfPCell(new Phrase(value1_3_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(7);
        table.addCell(cell);
        //列四、五、六、七、八
        // 为了使代码简洁，接下来的存值进行遍历
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put(title1_4_1, value1_4_1);
        map.put(title1_5_1, value1_5_1);
        map.put(title1_6_1, value1_6_1);
        map.put(title1_7_1, value1_7_1);
        map.put(title1_8_1, value1_8_1);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            cell = new PdfPCell(new Phrase(entry.getKey(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            //列高
            cell.setFixedHeight(contentHeight50);
            cell.setColspan(5);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(entry.getValue(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            //列高
            cell.setFixedHeight(contentHeight50);
            cell.setColspan(20);
            table.addCell(cell);
        }

        //列九
        //主 要 学 习 及 工 作 经 历 （从本科开始填写，含国内进修情况按时间正序连续填写）
        cell = new PdfPCell(new Phrase(title1_9_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十
        //自何年月
        cell = new PdfPCell(new Phrase(title1_10_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        table.addCell(cell);

        //至何年月
        cell = new PdfPCell(new Phrase(title1_10_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        table.addCell(cell);

        //在何地、何学校、何单位任职 （或学习）
        cell = new PdfPCell(new Phrase(title1_10_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(14);
        table.addCell(cell);
        //证明人
        cell = new PdfPCell(new Phrase(title1_10_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(3);
        table.addCell(cell);
        //列十 内容
        for (TableValue item : tableValueList1) {
            //1
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight30);
            cell.setColspan(4);
            table.addCell(cell);

            //2 至何年月
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight30);
            cell.setColspan(4);
            table.addCell(cell);

            //3 在何地、何学校、何单位任职 （或学习）
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight30);
            cell.setColspan(14);
            table.addCell(cell);
            //4 证明人
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight30);
            cell.setColspan(3);
            table.addCell(cell);
        }

        //如何控制分页展示table，显得紧凑些？在add到document之前添加跨页设置
        //table.setSplitLate(false);//跨页处理
        //table.setSplitRows(true);
        document.add(table);
        //endregion

        //region 第二页
        String title2_1_1 = "个人思想政治及师德师风表现情况";
        String title2_2_1 = "申报拟聘岗位理由（限3000字以内）\n（根据本人情况如实填写，包括满足申报条件情况，重点突出在教学、科研、社会服务等方面取得的主要成果）";
        String value2_1_1 = "value2_1_1";
        String value2_2_1 = "value2_2_1";
        //个人思想政治及师德师风表现情况
        document.newPage();
        table = new PdfPTable(1);
        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        table.setWidths(new int[]{1});

        //列一
        //个人思想政治及师德师风表现情况
        cell = new PdfPCell(new Phrase(title2_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(value2_1_1, font));
        cell.setPadding(5);
        cell.setFixedHeight(200);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(title2_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(value2_2_1, font));
        cell.setPadding(5);
        cell.setFixedHeight(450);
        table.addCell(cell);
        document.add(table);
        //endregion

        //region 第三页
        String title3_1_1 = "近五年（任现职不满五年的按任现职以来）完成本科教学情况";
        String title3_2_1 = "序\n号";
        String title3_2_2 = "课程名称";
        String title3_2_3 = "起步年月";
        String title3_2_4 = "课程类别";
        String title3_2_5 = "学生\n人数";
        String title3_2_6 = "总学时\n（周）数";
        String title3_2_7 = "本人承担学\n时（周）数";
        String title3_2_8 = "教学\n评分";

        String title3_3_1 = "任现职以来承担的本科教学改革及建设项目";
        String title3_4_1 = "序号";
        String title3_4_2 = "项目名称";
        String title3_4_3 = "项目性质及来源";
        String title3_4_4 = "合同经费/\n实到经费";
        String title3_4_5 = "批准年月";
        String title3_4_6 = "起止年月";
        String title3_4_7 = "本人\n排名";
        String title3_5_1 = "任现职以来本科教学工作获奖情况";
        String title3_6_1 = "序号";
        String title3_6_2 = "获奖项目名称";
        String title3_6_3 = "奖项级别\n及等级";
        String title3_6_4 = "授奖部门";
        String title3_6_5 = "获奖\n年月";
        String title3_6_6 = "本人\n排名";
        String title3_7_1 = "近五年（任现职不满五年的按现任职以来）\n教学工作在本单位总体评价情况";
        String title3_7_2 = "_________%";
        String title3_7_3 = "教务处负责人签字：\n（公章）";
        String title3_8_1 = "任现职以来完成研究生教学、人才培养情况";
        String title3_9_1 = "序号";
        String title3_9_2 = "课程名称";
        String title3_9_3 = "起止年月";
        String title3_9_4 = "课程类别";
        String title3_9_5 = "学生\n人数";
        String title3_9_6 = "总学\n时（周）数";
        String title3_9_7 = "本人承担学时\n（周）数";
        List<TableValue> tableValueList3_1 = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            TableValue completeSchoolWork = new TableValue();
            completeSchoolWork.setField1("0" + i);
            completeSchoolWork.setField2("微积分" + i);
            completeSchoolWork.setField3("201309-20140" + i);
            completeSchoolWork.setField4("理论课（通识教学基础课）" + i);
            completeSchoolWork.setField5("60");
            completeSchoolWork.setField6("88学时" + i);
            completeSchoolWork.setField7("60学时" + i);
            completeSchoolWork.setField8("94" + i);
            tableValueList3_1.add(completeSchoolWork);
        }

        List<TableValue> tableValueList3_2 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TableValue buildProject = new TableValue();
            buildProject.setField1("1" + i);
            buildProject.setField2("秘密项目" + i);
            buildProject.setField3("学习完成任务" + i);
            buildProject.setField4("私有合同" + i);
            buildProject.setField5("202011" + i);
            buildProject.setField6("202001" + i);
            buildProject.setField7("第" + i);
            tableValueList3_2.add(buildProject);
        }

        List<TableValue> tableValueList3_3 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TableValue jxGzHuoJiangQingKuang = new TableValue();
            jxGzHuoJiangQingKuang.setField1("1" + i);
            jxGzHuoJiangQingKuang.setField2("秘密项目" + i);
            jxGzHuoJiangQingKuang.setField3("超级级别" + i);
            jxGzHuoJiangQingKuang.setField4("超好部门" + i);
            jxGzHuoJiangQingKuang.setField5("202001" + i);
            jxGzHuoJiangQingKuang.setField6("第" + i);
            tableValueList3_3.add(jxGzHuoJiangQingKuang);
        }

        List<TableValue> tableValueList3_4 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TableValue jxRcpeiyangqingk = new TableValue();
            jxRcpeiyangqingk.setField1("1" + i);
            jxRcpeiyangqingk.setField2("好课程" + i);
            jxRcpeiyangqingk.setField3("202003" + i);
            jxRcpeiyangqingk.setField4("研究科技" + i);
            jxRcpeiyangqingk.setField5("1" + i);
            jxRcpeiyangqingk.setField6("33学时" + i);
            jxRcpeiyangqingk.setField7("22学时" + i);
            tableValueList3_4.add(jxRcpeiyangqingk);
        }

        document.newPage();
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);
        //列一
        //近五年（任现职不满五年的按任现职以来）完成本科教学情况
        cell = new PdfPCell(new Phrase(title3_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //序号
        cell = new PdfPCell(new Phrase(title3_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);

        //课程名称
        cell = new PdfPCell(new Phrase(title3_2_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(5);
        table.addCell(cell);

        //起步年月
        cell = new PdfPCell(new Phrase(title3_2_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //课程类别
        cell = new PdfPCell(new Phrase(title3_2_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(6);
        table.addCell(cell);
        //学生人数
        cell = new PdfPCell(new Phrase(title3_2_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //总学时（周）数
        cell = new PdfPCell(new Phrase(title3_2_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //本人承担学时（周）数
        cell = new PdfPCell(new Phrase(title3_2_7, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //教学评分
        cell = new PdfPCell(new Phrase(title3_2_8, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);

        for (TableValue item : tableValueList3_1) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);

            //2 课程名称
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(5);
            table.addCell(cell);

            //3 起步年月
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //4 课程类别
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(6);
            table.addCell(cell);
            //5 学生人数
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //6 总学时（周）数
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //7 本人承担学时（周）数
            cell = new PdfPCell(new Phrase(item.getField7(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //8 教学评分
            cell = new PdfPCell(new Phrase(item.getField8(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
        }

        //列三
        //任现职以来承担的本科教学改革及建设项目
        cell = new PdfPCell(new Phrase(title3_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);
        //列四
        //序号
        cell = new PdfPCell(new Phrase(title3_4_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);
        //项目名称
        cell = new PdfPCell(new Phrase(title3_4_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(8);
        table.addCell(cell);
        //项目性质及来源
        cell = new PdfPCell(new Phrase(title3_4_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(5);
        table.addCell(cell);
        //合同经费/实到经费
        cell = new PdfPCell(new Phrase(title3_4_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //批准年月
        cell = new PdfPCell(new Phrase(title3_4_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //起止年月
        cell = new PdfPCell(new Phrase(title3_4_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //本人排名
        cell = new PdfPCell(new Phrase(title3_4_7, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);

        for (TableValue item : tableValueList3_2) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);
            //2 项目名称
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(8);
            table.addCell(cell);
            //3 项目性质及来源
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(5);
            table.addCell(cell);
            //4 合同经费/实到经费
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //5 批准年月
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //6 起止年月
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //7 本人排名
            cell = new PdfPCell(new Phrase(item.getField7(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
        }

        //列五
        //任现职以来本科教学工作获奖情况
        cell = new PdfPCell(new Phrase(title3_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列六
        //序号
        cell = new PdfPCell(new Phrase(title3_6_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);
        //获奖项目名称
        cell = new PdfPCell(new Phrase(title3_6_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(10);
        table.addCell(cell);
        //奖项级别及等级
        cell = new PdfPCell(new Phrase(title3_6_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(6);
        table.addCell(cell);
        //授奖部门
        cell = new PdfPCell(new Phrase(title3_6_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //获奖年月
        cell = new PdfPCell(new Phrase(title3_6_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //本人排名
        cell = new PdfPCell(new Phrase(title3_6_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);

        for (TableValue item : tableValueList3_3) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);
            //2 获奖项目名称
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(10);
            table.addCell(cell);
            //3 奖项级别及等级
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(6);
            table.addCell(cell);
            //4 授奖部门
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //5 获奖年月
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //6 本人排名
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
        }
        //列七
        //近五年（任现职不满五年的按现任职以来）教学工作在本单位总体评价情况
        cell = new PdfPCell(new Phrase(title3_7_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(11);
        table.addCell(cell);
        //________%
        cell = new PdfPCell(new Phrase(title3_7_2, fontBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidthRight(0);
        cell.setPaddingBottom(15);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        table.addCell(cell);
        //教务处负责人签字：（公章）
        cell = new PdfPCell(new Phrase(title3_7_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(8);
        table.addCell(cell);
        //空白
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthLeft(0);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(2);
        table.addCell(cell);

        //列八
        //任现职以来完成研究生教学、人才培养情况
        cell = new PdfPCell(new Phrase(title3_8_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);
        //列九
        //序号
        cell = new PdfPCell(new Phrase(title3_9_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);
        //课程名称
        cell = new PdfPCell(new Phrase(title3_9_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(8);
        table.addCell(cell);
        //起止年月
        cell = new PdfPCell(new Phrase(title3_9_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //课程类别
        cell = new PdfPCell(new Phrase(title3_9_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(4);
        table.addCell(cell);
        //学生人数
        cell = new PdfPCell(new Phrase(title3_9_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //总学时（周）数
        cell = new PdfPCell(new Phrase(title3_9_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //本人承担学时（周）数
        cell = new PdfPCell(new Phrase(title3_9_7, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(4);
        table.addCell(cell);

        for (TableValue item : tableValueList3_4) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);
            //2 课程名称
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(8);
            table.addCell(cell);
            //3 起止年月
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //4 课程类别
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(4);
            table.addCell(cell);
            //5 学生人数
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //6 总学时（周）数
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //7 本人承担学时（周）数
            cell = new PdfPCell(new Phrase(item.getField7(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(4);
            table.addCell(cell);
        }

        document.add(table);
        //endregion

        //region 第四页
        String title4_1_1 = "任现职以来独立指导研究生情况";
        String title4_2_1 = "在读人数";
        String title4_2_2 = "博士";
        String title4_2_3 = "硕士";
        String title4_2_4 = "已毕业人数";
        String title4_2_5 = "博士";
        String title4_2_6 = "硕士";
        String title4_3_1 = "已毕业硕士、博士论文获奖情况";
        String title4_4_1 = "任现职以来发表的教学论文、出版教材等\n（仅填写第一或通讯作者论文）";
        String title4_5_1 = "序号";
        String title4_5_2 = "论文（教材）名";
        String title4_5_3 = "期刊名或\n出版社";
        String title4_5_4 = "期刊号或书\n号";
        String title4_5_5 = "发表年月\n或出版年月";
        String title4_5_6 = "收录\n情况";
        String title4_5_7 = "期刊影\n响因子";
        String title4_5_8 = "他引\n次数";
        String title4_5_9 = "第一或\n通讯作\n者";
        String title4_6_1 = "任现职以来发表的科研论文、出版著作等\n（仅填写第一或通讯作者论文，限填15篇，按重要性排序）";
        String title4_7_1 = "序号";
        String title4_7_2 = "论文（著作）名";
        String title4_7_3 = "期刊名或\n出版社";
        String title4_7_4 = "期刊号或书\n号";
        String title4_7_5 = "发表年月\n或出版年月";
        String title4_7_6 = "收录情\n况影响\n因子";
        String title4_7_7 = "是否国\n际一流\n期刊";
        String title4_7_8 = "他引\n次数";
        String title4_7_9 = "第一或\n通讯作\n者";

        String value4_2_1 = "value4_2_1";
        String value4_2_2 = "value4_2_2";
        String value4_3_1 = "value4_3_1";
        String value4_3_2 = "value4_3_2";
        String value4_4_1 = "value4_4_1";

        List<TableValue> tableValueList4_1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TableValue lunWenChuBan = new TableValue();
            lunWenChuBan.setField1("1" + i);
            lunWenChuBan.setField2("*******" + i);
            lunWenChuBan.setField3("JACS-A" + i);
            lunWenChuBan.setField4("0002-8788" + i);
            lunWenChuBan.setField5("2013.03" + i);
            lunWenChuBan.setField6("SCI10.66" + i);
            lunWenChuBan.setField7("是" + i);
            lunWenChuBan.setField8("SCI20" + i);
            lunWenChuBan.setField9("2个通讯之一" + i);
            tableValueList4_1.add(lunWenChuBan);
        }

        document.newPage();
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //任现职以来独立指导研究生情况
        cell = new PdfPCell(new Phrase(title4_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //在职人数
        cell = new PdfPCell(new Phrase(title4_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        cell.setRowspan(2);
        table.addCell(cell);
        //博士
        cell = new PdfPCell(new Phrase(title4_2_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        table.addCell(cell);
        //value4_2_1
        cell = new PdfPCell(new Phrase(value4_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(5);
        table.addCell(cell);
        //已毕业人数
        cell = new PdfPCell(new Phrase(title4_2_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        cell.setRowspan(2);
        table.addCell(cell);
        //博士
        cell = new PdfPCell(new Phrase(title4_2_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        table.addCell(cell);
        //value4_2_2
        cell = new PdfPCell(new Phrase(value4_2_2, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        table.addCell(cell);
        //硕士
        cell = new PdfPCell(new Phrase(title4_2_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        table.addCell(cell);
        //value4_3_1
        cell = new PdfPCell(new Phrase(value4_3_1, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(5);
        table.addCell(cell);
        //硕士
        cell = new PdfPCell(new Phrase(title4_2_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        table.addCell(cell);
        //value4_3_2
        cell = new PdfPCell(new Phrase(value4_3_2, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        table.addCell(cell);

        //列三
        //已毕业硕士、博士论文获奖情况
        cell = new PdfPCell(new Phrase(title4_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(8);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(value4_4_1, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(17);
        table.addCell(cell);

        //列四
        //任现职以来发表的教学论文、出版教材等（仅填写第一或通讯作者论文）
        cell = new PdfPCell(new Phrase(title4_4_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列五
        //序号
        cell = new PdfPCell(new Phrase(title4_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);
        //论文（教材）名
        cell = new PdfPCell(new Phrase(title4_5_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(7);
        table.addCell(cell);
        //期刊名或出版社
        cell = new PdfPCell(new Phrase(title4_5_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //期刊号或书号
        cell = new PdfPCell(new Phrase(title4_5_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //发表年月或出版年月
        cell = new PdfPCell(new Phrase(title4_5_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //收录情况
        cell = new PdfPCell(new Phrase(title4_5_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //期刊影响因子
        cell = new PdfPCell(new Phrase(title4_5_7, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //他引次数
        cell = new PdfPCell(new Phrase(title4_5_8, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //第一或通讯作者
        cell = new PdfPCell(new Phrase(title4_5_9, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);

        for (TableValue item : tableValueList4_1) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);
            //2 论文（教材）名
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(7);
            table.addCell(cell);
            //3 期刊名或出版社
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //4 期刊号或书号
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //5 发表年月或出版年月
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //6 收录情况
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //7 期刊影响因子
            cell = new PdfPCell(new Phrase(item.getField7(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //8 他引次数
            cell = new PdfPCell(new Phrase(item.getField8(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //9 第一或通讯作者
            cell = new PdfPCell(new Phrase(item.getField9(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
        }

        //列六
        //任现职以来发表的科研论文、出版著作等（仅填写第一或通讯作者论文，限填15篇，按重要性排序）
        cell = new PdfPCell(new Phrase(title4_6_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列七
        //序号
        cell = new PdfPCell(new Phrase(title4_7_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);
        //论文（著作）名
        cell = new PdfPCell(new Phrase(title4_7_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(7);
        table.addCell(cell);
        //期刊名或出版社
        cell = new PdfPCell(new Phrase(title4_7_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //期刊号或书号
        cell = new PdfPCell(new Phrase(title4_7_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //发表年月或出版年月
        cell = new PdfPCell(new Phrase(title4_7_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //收录情况影响因子
        cell = new PdfPCell(new Phrase(title4_7_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //是否国际一流期刊
        cell = new PdfPCell(new Phrase(title4_7_7, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //他引次数
        cell = new PdfPCell(new Phrase(title4_7_8, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //第一或通讯作者
        cell = new PdfPCell(new Phrase(title4_7_9, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        for (TableValue item : tableValueList4_1) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);
            //2 论文（著作）名
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(7);
            table.addCell(cell);
            //3 期刊名或出版社
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //4 期刊号或书号
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //5 发表年月或出版年月
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //6 收录情况影响因子
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //7 是否国际一流期刊
            cell = new PdfPCell(new Phrase(item.getField7(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //8 他引次数
            cell = new PdfPCell(new Phrase(item.getField8(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //9 第一或通讯作者
            cell = new PdfPCell(new Phrase(item.getField9(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);

        }
        document.add(table);
        //endregion

        //region 第五页
        String title5_1_1 = "任现职以来承担的主要科研项目";
        String title5_2_1 = "序号";
        String title5_2_2 = "项目名称";
        String title5_2_3 = "项目性质及来源";
        String title5_2_4 = "合同经费/\n实到经费";
        String title5_2_5 = "批准年月";
        String title5_2_6 = "起止年月";
        String title5_2_7 = "本人\n排名";
        String title5_3_1 = "任现职以来科研获奖情况";
        String title5_4_1 = "序号";
        String title5_4_2 = "获奖项目名称";
        String title5_4_3 = "奖项级别\n及等级";
        String title5_4_4 = "授奖部门";
        String title5_4_5 = "获奖\n年月";
        String title5_4_6 = "本人\n排名";
        String title5_5_1 = "任现职以来授权专利情况";
        String title5_6_1 = "序号";
        String title5_6_2 = "专利号";
        String title5_6_3 = "专利名称";
        String title5_6_4 = "专利\n类别";
        String title5_6_5 = "授权\n年月";
        String title5_6_6 = "本人\n排名";
        String title5_6_7 = "是否\n转让";
        String title5_6_8 = "转让\n效益";
        String title5_7_1 = "其他工作及成果";

        String value5_1_1 = "value5_1_1";

        List<TableValue> tableValueList5_1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TableValue tableValue = new TableValue();
            tableValue.setField1("1" + i);
            tableValue.setField2("*************" + i);
            tableValue.setField3("国家字眼科学基金（面上项目）" + i);
            tableValue.setField4("60万/30万" + i);
            tableValue.setField5("2011.3.12" + i);
            tableValue.setField6("2020.01-2020-02" + i);
            tableValue.setField7("1" + i);
            tableValueList5_1.add(tableValue);
        }
        List<TableValue> tableValueList5_2 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TableValue tableValue = new TableValue();
            tableValue.setField1("1" + i);
            tableValue.setField2("*************" + i);
            tableValue.setField3("湖北省科技进步奖一等奖" + i);
            tableValue.setField4("湖北省政府" + i);
            tableValue.setField5("2011.03" + i);
            tableValue.setField6("2" + i);
            tableValueList5_2.add(tableValue);
        }
        List<TableValue> tableValueList5_3 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TableValue tableValue = new TableValue();
            tableValue.setField1("1" + i);
            tableValue.setField2("******" + i);
            tableValue.setField3("******" + i);
            tableValue.setField4("发明专利" + i);
            tableValue.setField5("202011" + i);
            tableValue.setField6("2" + i);
            tableValue.setField7("否" + i);
            tableValue.setField8("ly" + i);
            tableValueList5_3.add(tableValue);
        }
        document.newPage();
        numColumns = 25;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        cell = new PdfPCell(new Phrase(title5_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //序号
        cell = new PdfPCell(new Phrase(title5_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);
        //项目名称
        cell = new PdfPCell(new Phrase(title5_2_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(7);
        table.addCell(cell);
        //项目性质及来源
        cell = new PdfPCell(new Phrase(title5_2_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(5);
        table.addCell(cell);
        //合同经费/实到经费
        cell = new PdfPCell(new Phrase(title5_2_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //批准年月
        cell = new PdfPCell(new Phrase(title5_2_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //起止年月
        cell = new PdfPCell(new Phrase(title5_2_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(4);
        table.addCell(cell);
        //本人排名
        cell = new PdfPCell(new Phrase(title5_2_7, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);

        for (TableValue item : tableValueList5_1) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);
            //2 项目名称
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(7);
            table.addCell(cell);
            //3 项目性质及来源
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(5);
            table.addCell(cell);
            //4 合同经费/实到经费
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //5 批准年月
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //6 起止年月
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(4);
            table.addCell(cell);
            //7 本人排名
            cell = new PdfPCell(new Phrase(item.getField7(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
        }

        //列三
        cell = new PdfPCell(new Phrase(title5_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列四
        //1 序号
        cell = new PdfPCell(new Phrase(title5_4_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);
        //2 获奖项目名称
        cell = new PdfPCell(new Phrase(title5_4_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(9);
        table.addCell(cell);
        //3 奖项级别及等级
        cell = new PdfPCell(new Phrase(title5_4_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(7);
        table.addCell(cell);
        //4 授奖部门
        cell = new PdfPCell(new Phrase(title5_4_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //5 获奖年月
        cell = new PdfPCell(new Phrase(title5_4_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //6 本人排名
        cell = new PdfPCell(new Phrase(title5_4_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);

        for (TableValue item : tableValueList5_2) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);
            //2 获奖项目名称
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(9);
            table.addCell(cell);
            //3 奖项级别及等级
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(7);
            table.addCell(cell);
            //4 授奖部门
            cell = new PdfPCell(new Phrase(title5_4_4, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //5 获奖年月
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //6 本人排名
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
        }

        //列五
        cell = new PdfPCell(new Phrase(title5_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列六
        //1 序号
        cell = new PdfPCell(new Phrase(title5_6_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);
        //2 专利号
        cell = new PdfPCell(new Phrase(title5_6_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(5);
        table.addCell(cell);
        //3 专利名称
        cell = new PdfPCell(new Phrase(title5_6_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(4);
        table.addCell(cell);
        //4 专利类别
        cell = new PdfPCell(new Phrase(title5_6_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(6);
        table.addCell(cell);
        //5 授权年月
        cell = new PdfPCell(new Phrase(title5_6_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //6 本人排名
        cell = new PdfPCell(new Phrase(title5_6_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //7 是否转让
        cell = new PdfPCell(new Phrase(title5_6_7, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //8 转让效益
        cell = new PdfPCell(new Phrase(title5_6_8, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);

        for (TableValue item : tableValueList5_3) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);
            //2 专利号
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(5);
            table.addCell(cell);
            //3 专利名称
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(4);
            table.addCell(cell);
            //4 专利类别
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(6);
            table.addCell(cell);
            //5 授权年月
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //6 本人排名
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //7 是否转让
            cell = new PdfPCell(new Phrase(item.getField7(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //8 转让效益
            cell = new PdfPCell(new Phrase(item.getField8(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
        }

        //列七
        cell = new PdfPCell(new Phrase(title5_7_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列八
        cell = new PdfPCell(new Phrase(value5_1_1, font));
        cell.setPadding(5);
        cell.setFixedHeight(90);
        cell.setColspan(numColumns);
        table.addCell(cell);

        document.add(table);
        //endregion

        //region 第六页
        String title6_1_1 = "拟聘岗位工作思路及预期目标（限2000字以内）";
        String value6_1_1 = "value6_1_1";
        String title6_2_1 = "个人承诺";
        String title6_3_1 = "          本人慎重承诺所从事的学术研究符合学术道德规范，所填写内容真实准确，如有不实之处，本人愿意承担相关责任。";
        String title6_4_1 = "_______________________（本人签名）";
        String title6_5_1 = "年                月                日";
        document.newPage();
        numColumns = 1;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        cell = new PdfPCell(new Phrase(title6_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(value6_1_1, font));
        cell.setPadding(5);
        cell.setFixedHeight(550);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        cell = new PdfPCell(new Phrase(title6_2_1, fontBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthBottom(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列三
        cell = new PdfPCell(new Phrase(title6_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setPaddingLeft(8);
        cell.setPaddingRight(8);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列四
        cell = new PdfPCell(new Phrase(title6_4_1, fontBold));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(10);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列五
        cell = new PdfPCell(new Phrase(title6_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(10);
        cell.setColspan(numColumns);
        table.addCell(cell);
        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setFixedHeight(contentHeight6);
        cell.setBorderWidthTop(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        document.add(table);
        //endregion

        //region 第七页
        String title7_1_1 = "基层党支部审核意见\n（对申报人的思想政治、师德师风及日常综合表现给予评价，是否同意申报人申请高一级职务）";
        String title7_2_1 = "基层党支部负责人_____________________（签名）";
        String title7_3_1 = "年                月                日";
        String title7_4_1 = "基层党委（总支）审核意见\n（是否同意基层党支部对申报人的评价鉴定，是否同意申报人申请高一级职务）";
        String title7_5_1 = "基层党委（总支）负责人_____________________（签名）";
        String title7_6_1 = "公         章：";
        String title7_7_1 = "年                月                日";
        String title7_8_1 = "院    系（所）   审    查    意    见\n（提供材料是否真实有效，是否符合申报岗位条件等）";
        String title7_9_1 = "材 料 审 核 人：_____________________（签名）";
        String title7_10_1 = "院、系（所）负责人_____________________（签名）";
        String title7_11_1 = "公    章：";
        String title7_12_1 = "年                月                日";

        document.newPage();
        numColumns = 1;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //基层党支部审核意见（对申报人的思想政治、师德师风及日常综合表现给予评价，是否同意申报人申请高一级职务）
        cell = new PdfPCell(new Phrase(title7_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(120);
        cell.setBorderWidthBottom(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //基层党支部负责人_____________________（签名）
        cell = new PdfPCell(new Phrase(title7_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(8);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列三
        //年月日
        cell = new PdfPCell(new Phrase(title7_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(70);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setFixedHeight(contentHeight6);
        cell.setBorderWidthTop(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //四
        //基层党委（总支）审核意见（是否同意基层党支部对申报人的评价鉴定，是否同意申报人申请高一级职务）
        cell = new PdfPCell(new Phrase(title7_4_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(110);
        cell.setBorderWidthBottom(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列五
        // 基层党委（总支）负责人_____________________（签名）
        cell = new PdfPCell(new Phrase(title7_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(8);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列六
        // 公章
        cell = new PdfPCell(new Phrase(title7_6_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列七
        //年月日
        cell = new PdfPCell(new Phrase(title7_7_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(70);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setFixedHeight(contentHeight6);
        cell.setBorderWidthTop(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列八
        //
        cell = new PdfPCell(new Phrase(title7_8_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(110);
        cell.setBorderWidthBottom(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列九
        // 材 料 审 核 人：_____________________（签名）
        cell = new PdfPCell(new Phrase(title7_9_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(8);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十
        // 院、系（所）负责人_____________________（签名）
        cell = new PdfPCell(new Phrase(title7_10_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(8);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十一
        // 公章
        cell = new PdfPCell(new Phrase(title7_11_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十二
        //年月日
        cell = new PdfPCell(new Phrase(title7_12_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(70);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setFixedHeight(10);
        cell.setBorderWidthTop(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        document.add(table);
        //endregion

        //region 第八页
        String title8_1_1 = "院、系（所）学术委员会评议意见";
        String title8_2_1 = "院、系（所）学术委员会主任_______________（签名）";
        String title8_3_1 = "年                月                日";
        String title8_4_1 = "总人数";
        String title8_4_2 = "参加人数";
        String title8_4_3 = "表    决    结    果";
        String title8_4_4 = "备注";
        String title8_5_1 = "同意\n人数";
        String title8_5_2 = "不同意\n人数";
        String title8_5_3 = "弃权\n人数";
        String title8_6_1 = "院、系（所）聘任组聘任意见";
        String title8_7_1 = "院、系（所）聘任组组长_______________（签名）";
        String title8_8_1 = "公      章";
        String title8_9_1 = "年                月                日";
        String title8_10_1 = "总人数";
        String title8_10_2 = "参加人数";
        String title8_10_3 = "表    决    结    果";
        String title8_10_4 = "备注";
        String title8_11_1 = "同意\n人数";
        String title8_11_2 = "不同意\n人数";
        String title8_11_3 = "弃权\n人数";

        document.newPage();
        numColumns = 25;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //院、系（所）学术委员会评议意见
        cell = new PdfPCell(new Phrase(title8_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthBottom(0);
        cell.setFixedHeight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //院、系（所）学术委员会主任_______________（签名）
        cell = new PdfPCell(new Phrase(title8_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(5);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列三
        //年月日
        cell = new PdfPCell(new Phrase(title8_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(20);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthTop(0);
        cell.setFixedHeight(contentHeight6);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列四
        //总人数
        cell = new PdfPCell(new Phrase(title8_4_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数
        cell = new PdfPCell(new Phrase(title8_4_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //表决结果
        cell = new PdfPCell(new Phrase(title8_4_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(12);
        table.addCell(cell);

        //备注
        cell = new PdfPCell(new Phrase(title8_4_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);

        //列四
        //总人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(4);
        table.addCell(cell);

        //同意人数
        cell = new PdfPCell(new Phrase(title8_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //不同意人数
        cell = new PdfPCell(new Phrase(title8_5_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //弃权人数
        cell = new PdfPCell(new Phrase(title8_5_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //备注 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(5);
        table.addCell(cell);

        //列六
        //院、系（所）聘任组聘任意见
        cell = new PdfPCell(new Phrase(title8_6_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthBottom(0);
        cell.setFixedHeight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列七
        //院、系（所）聘任组组长_______________（签名）
        cell = new PdfPCell(new Phrase(title8_7_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(5);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列八
        //公章
        cell = new PdfPCell(new Phrase(title8_8_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(140);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列九
        //年月日
        cell = new PdfPCell(new Phrase(title8_9_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(20);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthTop(0);
        cell.setFixedHeight(contentHeight6);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十
        //总人数
        cell = new PdfPCell(new Phrase(title8_10_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数
        cell = new PdfPCell(new Phrase(title8_10_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //表决结果
        cell = new PdfPCell(new Phrase(title8_10_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(12);
        table.addCell(cell);

        //备注
        cell = new PdfPCell(new Phrase(title8_10_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);

        //列十一
        //总人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        table.addCell(cell);

        //同意人数
        cell = new PdfPCell(new Phrase(title8_11_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //不同意人数
        cell = new PdfPCell(new Phrase(title8_11_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //弃权人数
        cell = new PdfPCell(new Phrase(title8_11_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //备注 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(5);
        table.addCell(cell);

        document.add(table);
        //endregion

        //region 第九页
        String title9_1_1 = "校学术评议组评议意见";
        String title9_2_1 = "校学术评议组组长_______________（签名）";
        String title9_3_1 = "年                月                日";
        String title9_4_1 = "总人数";
        String title9_4_2 = "参加人数";
        String title9_4_3 = "表    决    结    果";
        String title9_4_4 = "备注";
        String title9_5_1 = "同意\n人数";
        String title9_5_2 = "不同意\n人数";
        String title9_5_3 = "弃权\n人数";
        String title9_6_1 = "校聘任委员会聘任意见";
        String title9_7_1 = "校聘任委员会主任_______________（签名）";
        String title9_8_1 = "学 校 公 章";
        String title9_9_1 = "年                月                日";
        String title9_10_1 = "总人数";
        String title9_10_2 = "参加人数";
        String title9_10_3 = "表    决    结    果";
        String title9_10_4 = "备注";
        String title9_11_1 = "同意\n人数";
        String title9_11_2 = "不同意\n人数";
        String title9_11_3 = "弃权\n人数";

        document.newPage();
        numColumns = 25;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //校学术评议组评议意见
        cell = new PdfPCell(new Phrase(title9_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthBottom(0);
        cell.setFixedHeight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //校学术评议组组长_______________（签名）
        cell = new PdfPCell(new Phrase(title9_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(5);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列三
        //年月日
        cell = new PdfPCell(new Phrase(title9_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(20);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthTop(0);
        cell.setFixedHeight(contentHeight6);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列四
        //总人数
        cell = new PdfPCell(new Phrase(title9_4_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数
        cell = new PdfPCell(new Phrase(title9_4_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //表决结果
        cell = new PdfPCell(new Phrase(title9_4_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(12);
        table.addCell(cell);

        //备注
        cell = new PdfPCell(new Phrase(title9_4_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);

        //列四
        //总人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(4);
        table.addCell(cell);

        //同意人数
        cell = new PdfPCell(new Phrase(title9_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //不同意人数
        cell = new PdfPCell(new Phrase(title9_5_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //弃权人数
        cell = new PdfPCell(new Phrase(title9_5_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //备注 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(5);
        table.addCell(cell);

        //列六
        //校聘任委员会聘任意见
        cell = new PdfPCell(new Phrase(title9_6_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthBottom(0);
        cell.setFixedHeight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列七
        //校聘任委员会主任_______________（签名）
        cell = new PdfPCell(new Phrase(title9_7_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(5);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列八
        //学 校 公 章
        cell = new PdfPCell(new Phrase(title9_8_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(140);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列九
        //年月日
        cell = new PdfPCell(new Phrase(title9_9_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(20);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthTop(0);
        cell.setFixedHeight(contentHeight6);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十
        //总人数
        cell = new PdfPCell(new Phrase(title9_10_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数
        cell = new PdfPCell(new Phrase(title9_10_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //表决结果
        cell = new PdfPCell(new Phrase(title9_10_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(12);
        table.addCell(cell);

        //备注
        cell = new PdfPCell(new Phrase(title9_10_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);

        //列十一
        //总人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        table.addCell(cell);

        //同意人数
        cell = new PdfPCell(new Phrase(title9_11_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //不同意人数
        cell = new PdfPCell(new Phrase(title9_11_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //弃权人数
        cell = new PdfPCell(new Phrase(title9_11_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //备注 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(5);
        table.addCell(cell);

        document.add(table);
        //endregion

        //region 合并添加PDF
        if (mergeAddPdfList.size() > 0) {
            List<PdfReader> readers = new ArrayList<>();
            for (String fileurl : mergeAddPdfList) {
                PdfReader reader = new PdfReader(fileurl);
                readers.add(reader);
            }
            PdfContentByte cb = writer.getDirectContent();
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    PdfImportedPage page = writer.getImportedPage(pdfReader,
                            pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                }
                pageOfCurrentReaderPDF = 0;
            }
        }
        //endregion

        out.flush();
        document.close();
        out.close();
        //region 水印和页码
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        PdfReader reader = new PdfReader(fileName);
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outWatermarkFileName));
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            if (i >= 3) {
                // 文字水印
                PdfContentByte over2 = stamp.getOverContent(i);
                over2.beginText();
                // 设置颜色 默认为蓝色
                over2.setColorFill(BaseColor.BLACK);
                // 设置字体字号
                over2.setFontAndSize(bf, 12);
                // 设置起始位置
                over2.setTextMatrix(100, 20);
                over2.showTextAligned(Element.ALIGN_CENTER, "" + (i - 2), 295, 40, 0);
                over2.endText();
            }
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.5f);// 设置透明度为0.3

            // 文字水印
            PdfContentByte over = stamp.getOverContent(i);
            over.beginText();
            // 设置颜色 默认为蓝色
            over.setColorFill(BaseColor.LIGHT_GRAY);
            // 设置字体字号
            over.setFontAndSize(bf, 240);
            // 设置起始位置
            over.setTextMatrix(100, 200);
            over.setGState(gs);

            //over.showTextAligned(Element.ALIGN_CENTER, "武汉协和医院！", 170 + 150, 280, 30);

            over.showTextAligned(Element.ALIGN_CENTER, watermarkName, 170 + 180, 370, 45);
            over.endText();
        }
        stamp.close();
        reader.close();
        //endregion
    }

    public void writePdf1(String fileName) throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        FileOutputStream out = new FileOutputStream(fileName);
        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open(); // 文档里写入
        BaseFont baseFontChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(baseFontChinese, 11, normal, black);

        Font fontBold = new Font(baseFontChinese, 11, bold, black);
        Float contentHeight40 = 40f;
        Float contentHeight45 = 45f;
        Float contentHeight50 = 50f;
        Float contentHeight55 = 55f;
        Float contentHeight30 = 30f;
        Float contentHeight25 = 25f;
        Float contentHeight35 = 35f;
        Float contentHeight60 = 60f;
        Float contentHeight65 = 65f;
        Float contentHeight6 = 6f;

        int numColumns = 25;
        int totalWidth = 520;
        int[] setWids = new int[numColumns];
        PdfPTable table = null;

        PdfPCell cell;

        String coverHg = "___________________";
        //region 封面
        String titleCover_1 = "人事编号：__________";
        String titleCover_2 = "华中科技大学专业技术岗位\n申    报    表";
        String titleCover_3 = "姓        名" + coverHg;
        String titleCover_4 = "所 在 院";
        String titleCover_4_1 = "(系、所)" + coverHg;
        String titleCover_5 = "现任岗位";
        String titleCover_5_1 = "(职   务)" + coverHg;
        String titleCover_6 = "拟聘岗位";
        String titleCover_6_1 = "(职   务)" + coverHg;
        String titleCover_8 = "华中科技大学聘任委员会制";
        Font fontCover1 = new Font(baseFontChinese, 18, normal, black);
        Font fontCover2 = new Font(baseFontChinese, 25, bold, black);
        Font fontCover3 = new Font(baseFontChinese, 18, bold, black);

        document.newPage();
        numColumns = 1;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);

        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //人事编号______
        cell = new PdfPCell(new Phrase(titleCover_1, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(70);
        cell.setPaddingRight(30);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //华中科技大学专业技术岗位
        cell = new PdfPCell(new Phrase(titleCover_2, fontCover2));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(150);
        cell.setColspan(numColumns);
        table.addCell(cell);


        //姓        名
        cell = new PdfPCell(new Phrase(titleCover_3, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(80);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //所 在 院
        cell = new PdfPCell(new Phrase(titleCover_4, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingRight(175);
        cell.setFixedHeight(40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //(系、所)
        cell = new PdfPCell(new Phrase(titleCover_4_1, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //现任岗位
        cell = new PdfPCell(new Phrase(titleCover_5, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingTop(15);
        cell.setPaddingRight(175);
        cell.setFixedHeight(50);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //(职   务)
        cell = new PdfPCell(new Phrase(titleCover_5_1, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //拟聘岗位
        cell = new PdfPCell(new Phrase(titleCover_6, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingTop(15);
        cell.setPaddingRight(175);
        cell.setFixedHeight(50);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //(职   务)
        cell = new PdfPCell(new Phrase(titleCover_6_1, fontCover1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(40);
        cell.setColspan(numColumns);
        table.addCell(cell);


        //华中科技大学聘任委员会制
        cell = new PdfPCell(new Phrase(titleCover_8, fontCover3));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(150);
        cell.setColspan(numColumns);
        table.addCell(cell);


        document.add(table);

        //endregion

        //region 填表说明
        String strKongGe_1 = "      ";
        String titleExp_1 = "填  表  说  明";
        ArrayList<String> expTitleList = new ArrayList<>();
        expTitleList.add("（一）本表第1至6页由本人填写，所在院、系（所）审核。");
        expTitleList.add("（二）如填写内容较多，可另加附页。");
        expTitleList.add("（三）版面要求：用A4纸张大小，双面打印。");
        expTitleList.add("（四）本表适用于非专任教师申请专业技术岗位人员填报。");

        Font fontExpTitle = new Font(baseFontChinese, 15, bold, black);
        Font fontExpContent = new Font(baseFontChinese, 12, normal, black);
        float expPaddingLeft = 20f;
        float expPaddingRight = 10f;
        float contentHeight = 33f;
        document.newPage();
        numColumns = 1;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);

        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //填表说明
        cell = new PdfPCell(new Phrase(titleExp_1, fontExpTitle));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(100);
        cell.setPaddingTop(40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        for (String item : expTitleList) {
            cell = new PdfPCell(new Phrase(item, fontExpContent));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(expPaddingLeft);
            cell.setPaddingRight(expPaddingRight);
            cell.setFixedHeight(contentHeight);
            cell.setColspan(numColumns);
            table.addCell(cell);
        }

        document.add(table);
        //endregion

        //region 第一页
        document.newPage();
        numColumns = 25;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        String title1_1_1 = "姓        名";
        String title1_1_2 = "性别";
        String title1_1_3 = "出生年月";

        String title1_2_1 = "现专业技术岗位";
        String title1_2_2 = "聘任时间";
        String title1_3_1 = "来校工作时间";
        String title1_3_2 = "现从事专业\n及专长";
        String title1_4_1 = "社会兼职";
        String title1_5_1 = "何时何地受\n何奖励及处分";
        String title1_6_1 = "近五年考核\n情              况";
        String title1_9_1 = "主 要 学 习 及 工 作 经 历 （从本科开始填写，含国内进修情况按时间正序连续填写）";
        String title1_10_1 = "自何年月";
        String title1_10_2 = "至何年月";
        String title1_10_3 = "在何地、何学校、何单位任职 （或学习）";
        String title1_10_4 = "证明人";

        String value1_1_1 = "value1_1_1";
        String value1_1_2 = "value1_1_2";
        String value1_1_3 = "value1_1_3";
        String value1_2_1 = "value1_2_1";
        String value1_2_2 = "value1_2_2";
        String value1_3_1 = "value1_3_1";
        String value1_3_2 = "value1_3_2";
        String value1_4_1 = "value1_4_1";
        String value1_5_1 = "value1_5_1";
        String value1_6_1 = "value1_6_1";

        List<TableValue> tableValueList1 = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            TableValue tableValue = new TableValue();
            tableValue.setField1("1994.09" + i);
            tableValue.setField2("2001.06" + i);
            tableValue.setField3("清华大学 攻读学士学位" + i);
            tableValue.setField4("***" + i);
            tableValueList1.add(tableValue);
        }

        //列一
        //姓名
        cell = new PdfPCell(new Phrase(title1_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(5);
        table.addCell(cell);
        //value1_1_1
        cell = new PdfPCell(new Phrase(value1_1_1, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        table.addCell(cell);
        //性别
        cell = new PdfPCell(new Phrase(title1_1_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);
        //value1_1_2
        cell = new PdfPCell(new Phrase(value1_1_2, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(3);
        table.addCell(cell);
        //出生年月
        cell = new PdfPCell(new Phrase(title1_1_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(3);
        table.addCell(cell);
        //value1_1_3
        cell = new PdfPCell(new Phrase(value1_1_3, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        table.addCell(cell);
        //照片
        cell = new PdfPCell(new Phrase("照\n片", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        cell.setRowspan(3);
        table.addCell(cell);
        //列二
        //现专业技术岗位
        cell = new PdfPCell(new Phrase(title1_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);
        //value1_2_1
        cell = new PdfPCell(new Phrase(value1_2_1, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(6);
        table.addCell(cell);
        //聘任时间
        cell = new PdfPCell(new Phrase(title1_2_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(3);
        table.addCell(cell);
        //value1_2_2
        cell = new PdfPCell(new Phrase(value1_2_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(7);
        table.addCell(cell);

        //列三
        //来校工作时间
        cell = new PdfPCell(new Phrase(title1_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);
        //value1_3_1
        cell = new PdfPCell(new Phrase(value1_3_1, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(6);
        table.addCell(cell);
        //现从事专业及专长
        cell = new PdfPCell(new Phrase(title1_3_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(3);
        table.addCell(cell);
        //value1_3_2
        cell = new PdfPCell(new Phrase(value1_3_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(7);
        table.addCell(cell);
        //列四、五、六
        // 为了使代码简洁，接下来的存值进行遍历
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put(title1_4_1, value1_4_1);
        map.put(title1_5_1, value1_5_1);
        map.put(title1_6_1, value1_6_1);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            cell = new PdfPCell(new Phrase(entry.getKey(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            //列高
            cell.setFixedHeight(75f);
            cell.setColspan(5);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(entry.getValue(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            //列高
            cell.setFixedHeight(75f);
            cell.setColspan(20);
            table.addCell(cell);
        }

        //列九
        //主 要 学 习 及 工 作 经 历 （从本科开始填写，含国内进修情况按时间正序连续填写）
        cell = new PdfPCell(new Phrase(title1_9_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十
        //自何年月
        cell = new PdfPCell(new Phrase(title1_10_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        table.addCell(cell);

        //至何年月
        cell = new PdfPCell(new Phrase(title1_10_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(4);
        table.addCell(cell);

        //在何地、何学校、何单位任职 （或学习）
        cell = new PdfPCell(new Phrase(title1_10_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(14);
        table.addCell(cell);
        //证明人
        cell = new PdfPCell(new Phrase(title1_10_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(3);
        table.addCell(cell);
        //列十 内容
        for (TableValue item : tableValueList1) {
            //1
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight30);
            cell.setColspan(4);
            table.addCell(cell);

            //2 至何年月
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight30);
            cell.setColspan(4);
            table.addCell(cell);

            //3 在何地、何学校、何单位任职 （或学习）
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight30);
            cell.setColspan(14);
            table.addCell(cell);
            //4 证明人
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight30);
            cell.setColspan(3);
            table.addCell(cell);
        }

        //如何控制分页展示table，显得紧凑些？在add到document之前添加跨页设置
        //table.setSplitLate(false);//跨页处理
        //table.setSplitRows(true);
        document.add(table);
        //endregion

        //region 第二页
        String title2_1_1 = "个人思想政治及师德师风表现情况";
        String value2_1_1 = "value2_1_1";
        String title2_2_1 = "任现职以来完成教学、人才培养情况";
        String title2_3_1 = "起止年月";
        String title2_3_2 = "讲授课程名称及其它教学任务";
        String title2_3_3 = "学生\n人数";
        String title2_3_4 = "周学\n时数";
        String title2_3_5 = "总学\n时数";
        String title2_3_6 = "备      注";

        List<TableValue> tableValueList2 = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            TableValue tableValue = new TableValue();
            tableValue.setField1("1994.09-1998.10" + i);
            tableValue.setField2("AAAAAAAAAAAA" + i);
            tableValue.setField3("1" + i);
            tableValue.setField4("2" + i);
            tableValue.setField5("3" + i);
            tableValue.setField6("备注——" + i);
            tableValueList2.add(tableValue);
        }

        document.newPage();
        numColumns = 25;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];
        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);

        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //个人思想政治及师德师风表现情况
        cell = new PdfPCell(new Phrase(title2_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(value2_1_1, font));
        cell.setPadding(5);
        cell.setFixedHeight(200);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(title2_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight45);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //起止年月
        cell = new PdfPCell(new Phrase(title2_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(4);
        table.addCell(cell);

        //讲授课程名称及其它教学任务
        cell = new PdfPCell(new Phrase(title2_3_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(8);
        table.addCell(cell);

        //学生人数
        cell = new PdfPCell(new Phrase(title2_3_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);

        //周学时分
        cell = new PdfPCell(new Phrase(title2_3_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);

        //总学时分
        cell = new PdfPCell(new Phrase(title2_3_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);

        //备注
        cell = new PdfPCell(new Phrase(title2_3_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(4);
        table.addCell(cell);

        for(TableValue item : tableValueList2){
            //起止年月
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight45);
            cell.setColspan(4);
            table.addCell(cell);

            //讲授课程名称及其它教学任务
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight45);
            cell.setColspan(8);
            table.addCell(cell);

            //学生人数
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight45);
            cell.setColspan(3);
            table.addCell(cell);

            //周学时分
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight45);
            cell.setColspan(3);
            table.addCell(cell);

            //总学时分
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight45);
            cell.setColspan(3);
            table.addCell(cell);

            //备注
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight45);
            cell.setColspan(4);
            table.addCell(cell);
        }

        document.add(table);
        //endregion

        //region 第三页
        String title3_1_1 = "任现职以来发表的论文、出版著作和教材（可续页）";
        String title3_2_1 = "序\n号";
        String title3_2_2 = "论著（教科书）名称";
        String title3_2_3 = "期刊名称\n（出版社、\n起止页码）";
        String title3_2_4 = "刊号\n（发表出版\n年月）";
        String title3_2_5 = "期刊\n级别";
        String title3_2_6 = "第几\n作者";

        List<TableValue> tableValueList3 = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            TableValue tableValue = new TableValue();
            tableValue.setField1("0" + i);
            tableValue.setField2("XXXXXXX" + i);
            tableValue.setField3("1-2" + i);
            tableValue.setField4("2020.1-2020.4" + i);
            tableValue.setField5("60");
            tableValue.setField6("AAA" + i);
            tableValueList3.add(tableValue);
        }

        document.newPage();
        numColumns = 25;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);
        //列一
        //任现职以来发表的论文、出版著作和教材（可续页）
        cell = new PdfPCell(new Phrase(title3_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //序号
        cell = new PdfPCell(new Phrase(title3_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(1);
        table.addCell(cell);

        //论著（教科书）名称
        cell = new PdfPCell(new Phrase(title3_2_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(10);
        table.addCell(cell);

        //期刊名称（出版社、起止页码）
        cell = new PdfPCell(new Phrase(title3_2_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(4);
        table.addCell(cell);

        //刊号（发表出版年月）
        cell = new PdfPCell(new Phrase(title3_2_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(4);
        table.addCell(cell);

        //期刊级别
        cell = new PdfPCell(new Phrase(title3_2_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(3);
        table.addCell(cell);

        //第几作者
        cell = new PdfPCell(new Phrase(title3_2_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(3);
        table.addCell(cell);

        for(TableValue item : tableValueList3){
            //序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(1);
            table.addCell(cell);

            //论著（教科书）名称
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(10);
            table.addCell(cell);

            //期刊名称（出版社、起止页码）
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(4);
            table.addCell(cell);

            //刊号（发表出版年月）
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(4);
            table.addCell(cell);

            //期刊级别
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(3);
            table.addCell(cell);

            //第几作者
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(3);
            table.addCell(cell);
        }

        document.add(table);
        //endregion

        //region 第四页
        String title4_1_1 = "任现职以来承担的主要科研项目";
        String title4_2_1 = "序\n号";
        String title4_2_2 = "项目名称";
        String title4_2_3 = "项目性质及\n来源";
        String title4_2_4 = "合同经费/实\n到经费";
        String title4_2_5 = "批准年月";
        String title4_2_6 = "起止年月";
        String title4_2_7 = "本人\n排名";

        List<TableValue> tableValueList4 = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            TableValue tableValue = new TableValue();
            tableValue.setField1("0" + i);
            tableValue.setField2("XXXXXXX" + i);
            tableValue.setField3("AAAAA" + i);
            tableValue.setField4("BBBBBB" + i);
            tableValue.setField5("2020.1-2020.4");
            tableValue.setField6("2020.1-2020.4" + i);
            tableValue.setField7("A" + i);
            tableValueList4.add(tableValue);
        }

        document.newPage();
        numColumns = 25;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);
        //列一
        //
        cell = new PdfPCell(new Phrase(title4_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //序号
        cell = new PdfPCell(new Phrase(title4_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(2);
        table.addCell(cell);

        //项目名称
        cell = new PdfPCell(new Phrase(title4_2_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(7);
        table.addCell(cell);

        //项目性质及来源
        cell = new PdfPCell(new Phrase(title4_2_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(4);
        table.addCell(cell);

        //合同经费/实到经费
        cell = new PdfPCell(new Phrase(title4_2_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(4);
        table.addCell(cell);

        //批准年月
        cell = new PdfPCell(new Phrase(title4_2_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(3);
        table.addCell(cell);

        //起止年月
        cell = new PdfPCell(new Phrase(title4_2_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(3);
        table.addCell(cell);

        //本人排名
        cell = new PdfPCell(new Phrase(title4_2_7, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight55);
        cell.setColspan(2);
        table.addCell(cell);

        for(TableValue item : tableValueList4){
            //序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(2);
            table.addCell(cell);

            //项目名称
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(7);
            table.addCell(cell);

            //项目性质及来源
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(4);
            table.addCell(cell);

            //合同经费/实到经费
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(4);
            table.addCell(cell);

            //批准年月
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(3);
            table.addCell(cell);

            //起止年月
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(3);
            table.addCell(cell);

            //本人排名
            cell = new PdfPCell(new Phrase(item.getField7(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight55);
            cell.setColspan(2);
            table.addCell(cell);
        }

        document.add(table);
        //endregion

        //region 第五页
        String title5_1_1 = "任现职以来承担的主要科研项目";
        String title5_2_1 = "序号";
        String title5_2_2 = "项目名称";
        String title5_2_3 = "项目性质及来源";
        String title5_2_4 = "合同经费/\n实到经费";
        String title5_2_5 = "批准年月";
        String title5_2_6 = "起止年月";
        String title5_2_7 = "本人\n排名";
        String title5_3_1 = "任现职以来科研获奖情况";
        String title5_4_1 = "序号";
        String title5_4_2 = "获奖项目名称";
        String title5_4_3 = "奖项级别\n及等级";
        String title5_4_4 = "授奖部门";
        String title5_4_5 = "获奖\n年月";
        String title5_4_6 = "本人\n排名";
        String title5_5_1 = "任现职以来授权专利情况";
        String title5_6_1 = "序号";
        String title5_6_2 = "专利号";
        String title5_6_3 = "专利名称";
        String title5_6_4 = "专利\n类别";
        String title5_6_5 = "授权\n年月";
        String title5_6_6 = "本人\n排名";
        String title5_6_7 = "是否\n转让";
        String title5_6_8 = "转让\n效益";
        String title5_7_1 = "其他工作及成果";

        String value5_1_1 = "value5_1_1";

        List<TableValue> tableValueList5_1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TableValue tableValue = new TableValue();
            tableValue.setField1("1" + i);
            tableValue.setField2("*************" + i);
            tableValue.setField3("国家字眼科学基金（面上项目）" + i);
            tableValue.setField4("60万/30万" + i);
            tableValue.setField5("2011.3.12" + i);
            tableValue.setField6("2020.01-2020-02" + i);
            tableValue.setField7("1" + i);
            tableValueList5_1.add(tableValue);
        }
        List<TableValue> tableValueList5_2 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TableValue tableValue = new TableValue();
            tableValue.setField1("1" + i);
            tableValue.setField2("*************" + i);
            tableValue.setField3("湖北省科技进步奖一等奖" + i);
            tableValue.setField4("湖北省政府" + i);
            tableValue.setField5("2011.03" + i);
            tableValue.setField6("2" + i);
            tableValueList5_2.add(tableValue);
        }
        List<TableValue> tableValueList5_3 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TableValue tableValue = new TableValue();
            tableValue.setField1("1" + i);
            tableValue.setField2("******" + i);
            tableValue.setField3("******" + i);
            tableValue.setField4("发明专利" + i);
            tableValue.setField5("202011" + i);
            tableValue.setField6("2" + i);
            tableValue.setField7("否" + i);
            tableValue.setField8("ly" + i);
            tableValueList5_3.add(tableValue);
        }
        document.newPage();
        numColumns = 25;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        cell = new PdfPCell(new Phrase(title5_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //序号
        cell = new PdfPCell(new Phrase(title5_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);
        //项目名称
        cell = new PdfPCell(new Phrase(title5_2_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(7);
        table.addCell(cell);
        //项目性质及来源
        cell = new PdfPCell(new Phrase(title5_2_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(5);
        table.addCell(cell);
        //合同经费/实到经费
        cell = new PdfPCell(new Phrase(title5_2_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //批准年月
        cell = new PdfPCell(new Phrase(title5_2_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //起止年月
        cell = new PdfPCell(new Phrase(title5_2_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(4);
        table.addCell(cell);
        //本人排名
        cell = new PdfPCell(new Phrase(title5_2_7, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);

        for (TableValue item : tableValueList5_1) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);
            //2 项目名称
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(7);
            table.addCell(cell);
            //3 项目性质及来源
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(5);
            table.addCell(cell);
            //4 合同经费/实到经费
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //5 批准年月
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //6 起止年月
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(4);
            table.addCell(cell);
            //7 本人排名
            cell = new PdfPCell(new Phrase(item.getField7(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
        }

        //列三
        cell = new PdfPCell(new Phrase(title5_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列四
        //1 序号
        cell = new PdfPCell(new Phrase(title5_4_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);
        //2 获奖项目名称
        cell = new PdfPCell(new Phrase(title5_4_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(9);
        table.addCell(cell);
        //3 奖项级别及等级
        cell = new PdfPCell(new Phrase(title5_4_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(7);
        table.addCell(cell);
        //4 授奖部门
        cell = new PdfPCell(new Phrase(title5_4_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //5 获奖年月
        cell = new PdfPCell(new Phrase(title5_4_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //6 本人排名
        cell = new PdfPCell(new Phrase(title5_4_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);

        for (TableValue item : tableValueList5_2) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);
            //2 获奖项目名称
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(9);
            table.addCell(cell);
            //3 奖项级别及等级
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(7);
            table.addCell(cell);
            //4 授奖部门
            cell = new PdfPCell(new Phrase(title5_4_4, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //5 获奖年月
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //6 本人排名
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
        }

        //列五
        cell = new PdfPCell(new Phrase(title5_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列六
        //1 序号
        cell = new PdfPCell(new Phrase(title5_6_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(1);
        table.addCell(cell);
        //2 专利号
        cell = new PdfPCell(new Phrase(title5_6_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(5);
        table.addCell(cell);
        //3 专利名称
        cell = new PdfPCell(new Phrase(title5_6_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(4);
        table.addCell(cell);
        //4 专利类别
        cell = new PdfPCell(new Phrase(title5_6_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(6);
        table.addCell(cell);
        //5 授权年月
        cell = new PdfPCell(new Phrase(title5_6_5, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(3);
        table.addCell(cell);
        //6 本人排名
        cell = new PdfPCell(new Phrase(title5_6_6, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //7 是否转让
        cell = new PdfPCell(new Phrase(title5_6_7, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);
        //8 转让效益
        cell = new PdfPCell(new Phrase(title5_6_8, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight35);
        cell.setColspan(2);
        table.addCell(cell);

        for (TableValue item : tableValueList5_3) {
            //1 序号
            cell = new PdfPCell(new Phrase(item.getField1(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(1);
            table.addCell(cell);
            //2 专利号
            cell = new PdfPCell(new Phrase(item.getField2(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(5);
            table.addCell(cell);
            //3 专利名称
            cell = new PdfPCell(new Phrase(item.getField3(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(4);
            table.addCell(cell);
            //4 专利类别
            cell = new PdfPCell(new Phrase(item.getField4(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(6);
            table.addCell(cell);
            //5 授权年月
            cell = new PdfPCell(new Phrase(item.getField5(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(3);
            table.addCell(cell);
            //6 本人排名
            cell = new PdfPCell(new Phrase(item.getField6(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //7 是否转让
            cell = new PdfPCell(new Phrase(item.getField7(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
            //8 转让效益
            cell = new PdfPCell(new Phrase(item.getField8(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(contentHeight35);
            cell.setColspan(2);
            table.addCell(cell);
        }

        //列七
        cell = new PdfPCell(new Phrase(title5_7_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列八
        cell = new PdfPCell(new Phrase(value5_1_1, font));
        cell.setPadding(5);
        cell.setFixedHeight(90);
        cell.setColspan(numColumns);
        table.addCell(cell);

        document.add(table);
        //endregion

        //region 第六页
        String title6_1_1 = "拟聘岗位工作思路及预期目标（限2000字以内）";
        String value6_1_1 = "value6_1_1";
        String title6_2_1 = "个人承诺";
        String title6_3_1 = "          本人慎重承诺所从事的学术研究符合学术道德规范，所填写内容真实准确，如有不实之处，本人愿意承担相关责任。";
        String title6_4_1 = "_______________________（本人签名）";
        String title6_5_1 = "年                月                日";
        document.newPage();
        numColumns = 1;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        cell = new PdfPCell(new Phrase(title6_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(value6_1_1, font));
        cell.setPadding(5);
        cell.setFixedHeight(550);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        cell = new PdfPCell(new Phrase(title6_2_1, fontBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthBottom(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列三
        cell = new PdfPCell(new Phrase(title6_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setPaddingLeft(8);
        cell.setPaddingRight(8);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列四
        cell = new PdfPCell(new Phrase(title6_4_1, fontBold));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(10);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列五
        cell = new PdfPCell(new Phrase(title6_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(10);
        cell.setColspan(numColumns);
        table.addCell(cell);
        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setFixedHeight(contentHeight6);
        cell.setBorderWidthTop(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        document.add(table);
        //endregion

        //region 第七页
        String title7_1_1 = "基层党支部审核意见\n（对申报人的思想政治、师德师风及日常综合表现给予评价，是否同意申报人申请高一级职务）";
        String title7_2_1 = "基层党支部负责人_____________________（签名）";
        String title7_3_1 = "年                月                日";
        String title7_4_1 = "基层党委（总支）审核意见\n（是否同意基层党支部对申报人的评价鉴定，是否同意申报人申请高一级职务）";
        String title7_5_1 = "基层党委（总支）负责人_____________________（签名）";
        String title7_6_1 = "公         章：";
        String title7_7_1 = "年                月                日";
        String title7_8_1 = "院    系（所）   审    查    意    见\n（提供材料是否真实有效，是否符合申报岗位条件等）";
        String title7_9_1 = "材 料 审 核 人：_____________________（签名）";
        String title7_10_1 = "院、系（所）负责人_____________________（签名）";
        String title7_11_1 = "公    章：";
        String title7_12_1 = "年                月                日";

        document.newPage();
        numColumns = 1;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //基层党支部审核意见（对申报人的思想政治、师德师风及日常综合表现给予评价，是否同意申报人申请高一级职务）
        cell = new PdfPCell(new Phrase(title7_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(120);
        cell.setBorderWidthBottom(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //基层党支部负责人_____________________（签名）
        cell = new PdfPCell(new Phrase(title7_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(8);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列三
        //年月日
        cell = new PdfPCell(new Phrase(title7_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(70);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setFixedHeight(contentHeight6);
        cell.setBorderWidthTop(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //四
        //基层党委（总支）审核意见（是否同意基层党支部对申报人的评价鉴定，是否同意申报人申请高一级职务）
        cell = new PdfPCell(new Phrase(title7_4_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(110);
        cell.setBorderWidthBottom(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列五
        // 基层党委（总支）负责人_____________________（签名）
        cell = new PdfPCell(new Phrase(title7_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(8);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列六
        // 公章
        cell = new PdfPCell(new Phrase(title7_6_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列七
        //年月日
        cell = new PdfPCell(new Phrase(title7_7_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(70);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setFixedHeight(contentHeight6);
        cell.setBorderWidthTop(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列八
        //
        cell = new PdfPCell(new Phrase(title7_8_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(110);
        cell.setBorderWidthBottom(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列九
        // 材 料 审 核 人：_____________________（签名）
        cell = new PdfPCell(new Phrase(title7_9_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight30);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(8);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十
        // 院、系（所）负责人_____________________（签名）
        cell = new PdfPCell(new Phrase(title7_10_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(8);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十一
        // 公章
        cell = new PdfPCell(new Phrase(title7_11_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十二
        //年月日
        cell = new PdfPCell(new Phrase(title7_12_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingRight(70);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setFixedHeight(10);
        cell.setBorderWidthTop(0);
        cell.setColspan(numColumns);
        table.addCell(cell);

        document.add(table);
        //endregion

        //region 第八页
        String title8_1_1 = "院、系（所）学术委员会评议意见";
        String title8_2_1 = "院、系（所）学术委员会主任_______________（签名）";
        String title8_3_1 = "年                月                日";
        String title8_4_1 = "总人数";
        String title8_4_2 = "参加人数";
        String title8_4_3 = "表    决    结    果";
        String title8_4_4 = "备注";
        String title8_5_1 = "同意\n人数";
        String title8_5_2 = "不同意\n人数";
        String title8_5_3 = "弃权\n人数";
        String title8_6_1 = "院、系（所）聘任组聘任意见";
        String title8_7_1 = "院、系（所）聘任组组长_______________（签名）";
        String title8_8_1 = "公      章";
        String title8_9_1 = "年                月                日";
        String title8_10_1 = "总人数";
        String title8_10_2 = "参加人数";
        String title8_10_3 = "表    决    结    果";
        String title8_10_4 = "备注";
        String title8_11_1 = "同意\n人数";
        String title8_11_2 = "不同意\n人数";
        String title8_11_3 = "弃权\n人数";

        document.newPage();
        numColumns = 25;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //院、系（所）学术委员会评议意见
        cell = new PdfPCell(new Phrase(title8_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthBottom(0);
        cell.setFixedHeight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //院、系（所）学术委员会主任_______________（签名）
        cell = new PdfPCell(new Phrase(title8_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(5);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列三
        //年月日
        cell = new PdfPCell(new Phrase(title8_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(20);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthTop(0);
        cell.setFixedHeight(contentHeight6);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列四
        //总人数
        cell = new PdfPCell(new Phrase(title8_4_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数
        cell = new PdfPCell(new Phrase(title8_4_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //表决结果
        cell = new PdfPCell(new Phrase(title8_4_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(12);
        table.addCell(cell);

        //备注
        cell = new PdfPCell(new Phrase(title8_4_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);

        //列四
        //总人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(4);
        table.addCell(cell);

        //同意人数
        cell = new PdfPCell(new Phrase(title8_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //不同意人数
        cell = new PdfPCell(new Phrase(title8_5_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //弃权人数
        cell = new PdfPCell(new Phrase(title8_5_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //备注 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(5);
        table.addCell(cell);

        //列六
        //院、系（所）聘任组聘任意见
        cell = new PdfPCell(new Phrase(title8_6_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthBottom(0);
        cell.setFixedHeight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列七
        //院、系（所）聘任组组长_______________（签名）
        cell = new PdfPCell(new Phrase(title8_7_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(5);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列八
        //公章
        cell = new PdfPCell(new Phrase(title8_8_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(140);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列九
        //年月日
        cell = new PdfPCell(new Phrase(title8_9_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(20);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthTop(0);
        cell.setFixedHeight(contentHeight6);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十
        //总人数
        cell = new PdfPCell(new Phrase(title8_10_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数
        cell = new PdfPCell(new Phrase(title8_10_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //表决结果
        cell = new PdfPCell(new Phrase(title8_10_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(12);
        table.addCell(cell);

        //备注
        cell = new PdfPCell(new Phrase(title8_10_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);

        //列十一
        //总人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        table.addCell(cell);

        //同意人数
        cell = new PdfPCell(new Phrase(title8_11_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //不同意人数
        cell = new PdfPCell(new Phrase(title8_11_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //弃权人数
        cell = new PdfPCell(new Phrase(title8_11_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //备注 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(5);
        table.addCell(cell);

        document.add(table);
        //endregion

        //region 第九页
        String title9_1_1 = "校学术评议组评议意见";
        String title9_2_1 = "校学术评议组组长_______________（签名）";
        String title9_3_1 = "年                月                日";
        String title9_4_1 = "总人数";
        String title9_4_2 = "参加人数";
        String title9_4_3 = "表    决    结    果";
        String title9_4_4 = "备注";
        String title9_5_1 = "同意\n人数";
        String title9_5_2 = "不同意\n人数";
        String title9_5_3 = "弃权\n人数";
        String title9_6_1 = "校聘任委员会聘任意见";
        String title9_7_1 = "校聘任委员会主任_______________（签名）";
        String title9_8_1 = "学 校 公 章";
        String title9_9_1 = "年                月                日";
        String title9_10_1 = "总人数";
        String title9_10_2 = "参加人数";
        String title9_10_3 = "表    决    结    果";
        String title9_10_4 = "备注";
        String title9_11_1 = "同意\n人数";
        String title9_11_2 = "不同意\n人数";
        String title9_11_3 = "弃权\n人数";

        document.newPage();
        numColumns = 25;
        table = new PdfPTable(numColumns);
        setWids = new int[numColumns];

        //table总Width宽度
        table.setTotalWidth(totalWidth);
        //设置总Width宽度 生效
        table.setLockedWidth(true);
        //列布局
        for (int i = 0; i < numColumns; i++) {
            setWids[i] = 1;
        }
        table.setWidths(setWids);

        //列一
        //校学术评议组评议意见
        cell = new PdfPCell(new Phrase(title9_1_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthBottom(0);
        cell.setFixedHeight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列二
        //校学术评议组组长_______________（签名）
        cell = new PdfPCell(new Phrase(title9_2_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(5);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列三
        //年月日
        cell = new PdfPCell(new Phrase(title9_3_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(20);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthTop(0);
        cell.setFixedHeight(contentHeight6);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列四
        //总人数
        cell = new PdfPCell(new Phrase(title9_4_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数
        cell = new PdfPCell(new Phrase(title9_4_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //表决结果
        cell = new PdfPCell(new Phrase(title9_4_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(12);
        table.addCell(cell);

        //备注
        cell = new PdfPCell(new Phrase(title9_4_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);

        //列四
        //总人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(4);
        table.addCell(cell);

        //同意人数
        cell = new PdfPCell(new Phrase(title9_5_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //不同意人数
        cell = new PdfPCell(new Phrase(title9_5_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //弃权人数
        cell = new PdfPCell(new Phrase(title9_5_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(2);
        table.addCell(cell);

        //备注 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight60);
        cell.setColspan(5);
        table.addCell(cell);

        //列六
        //校聘任委员会聘任意见
        cell = new PdfPCell(new Phrase(title9_6_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(numColumns);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthBottom(0);
        cell.setFixedHeight(160);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列七
        //校聘任委员会主任_______________（签名）
        cell = new PdfPCell(new Phrase(title9_7_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(5);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列八
        //学 校 公 章
        cell = new PdfPCell(new Phrase(title9_8_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(140);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列九
        //年月日
        cell = new PdfPCell(new Phrase(title9_9_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight25);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPaddingRight(20);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //空
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorderWidthTop(0);
        cell.setFixedHeight(contentHeight6);
        cell.setColspan(numColumns);
        table.addCell(cell);

        //列十
        //总人数
        cell = new PdfPCell(new Phrase(title9_10_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数
        cell = new PdfPCell(new Phrase(title9_10_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(4);
        table.addCell(cell);

        //表决结果
        cell = new PdfPCell(new Phrase(title9_10_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(12);
        table.addCell(cell);

        //备注
        cell = new PdfPCell(new Phrase(title9_10_4, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight40);
        cell.setColspan(5);
        table.addCell(cell);

        //列十一
        //总人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        table.addCell(cell);

        //参加人数 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(4);
        table.addCell(cell);

        //同意人数
        cell = new PdfPCell(new Phrase(title9_11_1, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //不同意人数
        cell = new PdfPCell(new Phrase(title9_11_2, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //弃权人数
        cell = new PdfPCell(new Phrase(title9_11_3, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(2);
        table.addCell(cell);

        //备注 value
        cell = new PdfPCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(contentHeight50);
        cell.setColspan(5);
        table.addCell(cell);

        document.add(table);
        //endregion

        out.flush();
        document.close();
        out.close();
    }

    public static void main(String[] args) throws Exception {
        PDFDemo pdfDemo = new PDFDemo();
        ArrayList<String> mergeAddPdfList = new ArrayList<>();
        mergeAddPdfList.add("D:\\work\\java\\html\\demo-URL.pdf");
        mergeAddPdfList.add("D:\\work\\java\\html\\tempcreatePDF.pdf");

        pdfDemo.writePdf("D:\\work\\java\\html\\demo-111111.pdf","D:\\work\\java\\html\\demo-222222.pdf", mergeAddPdfList, "2020");
        //pdfDemo.writePdf1("D:\\work\\java\\html\\demo-333333.pdf");
    }

    @Data
    public class TableValue {
        String field1;
        String field2;
        String field3;
        String field4;
        String field5;
        String field6;
        String field7;
        String field8;
        String field9;
    }
}
