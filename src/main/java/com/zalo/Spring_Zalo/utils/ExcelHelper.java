package com.zalo.Spring_Zalo.utils;

import com.zalo.Spring_Zalo.Entities.User;
import com.zalo.Spring_Zalo.Service.SequenceGeneratorService;
import com.zalo.Spring_Zalo.request.UserRequestImport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Service
public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "username", "fullname", "email", "password","role" };
    static String SHEET = "Sheet1";
    static Logger logger = LoggerFactory.getLogger(ExcelHelper.class);
    private static SequenceGeneratorService sequenceGeneratorService;

    public ExcelHelper(SequenceGeneratorService sequenceGeneratorService) {
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }



    public static List<UserRequestImport> excelToTutorials(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<UserRequestImport> users = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                UserRequestImport user = new UserRequestImport();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            logger.info("case 0: " + currentCell.getStringCellValue());
                            user.setUsername(currentCell.getStringCellValue());
                            break;

                        case 1:
                            logger.info("case 1: " + currentCell.getStringCellValue());
                            user.setFullname(currentCell.getStringCellValue());
                            break;

                        case 2:
                            logger.info("case 2: " + currentCell.getStringCellValue());
                            user.setEmail(currentCell.getStringCellValue());
                            break;

                        case 3:
                            logger.info("case 3: " + currentCell.getCellType());
                            switch (currentCell.getCellType()) {
                                case STRING:
                                    user.setPassword(currentCell.getStringCellValue());
                                    break;
                                case NUMERIC:
                                    String passwordStr = String.format("%.0f", currentCell.getNumericCellValue());
                                    user.setPassword(passwordStr);
                                    break;
                            }
                            break;
                        case 4:
                                user.setRole(currentCell.getStringCellValue());
                                break;
                        default:
                            break;
                    }

                    cellIdx++;
                }

                users.add(user);
            }

            workbook.close();

            return users;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}