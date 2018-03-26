import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Jacky on 3/22/2018.
 */
public class Main {
    private static final String FILE_ONE = "src/Data1.xlsx";
    private static final String FILE_TWO = "src/Data2.xlsx";
    public static void main(String[] args)
    {
        int index = 0;
        int row = 0;
        double[] numSetOne = new double[4];
        double[] numSetTwo = new double[4];
        String[] wordSetOne = new String[4];
        double[] numSetThree = new double[4];
        double[] numSetFour = new double[4];
        String[] wordSetTwo = new String[4];
        double[] finalSet1 = new double[4];
        double[] finalSet2 = new double[4];
        String[] finalSet3 = new String[4];

        try {

            FileInputStream excelFile = new FileInputStream(new File(FILE_ONE));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();

                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();
                    //index 0 is first row which contains the name not values

                    if(index ==0) {/*
                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
                            System.out.print(currentCell.getStringCellValue() + "--");
                        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            System.out.print(currentCell.getNumericCellValue() + "--");
                        }*/
                    }
                    else{
                        //if row = 2 then this is where we know in the file it is a string
                        if (currentCell.getCellTypeEnum() == CellType.STRING && row == 2) {
                            //System.out.print(currentCell.getStringCellValue() + "--");
                            wordSetOne[(index-1)] = currentCell.getStringCellValue();
                        }
                        else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            //System.out.print(currentCell.getNumericCellValue() + "--");
                            //check first row
                            if (row == 0) {
                                numSetOne[(index-1)] = currentCell.getNumericCellValue();
                            } //or second row
                            else if (row == 1) {
                                numSetTwo[(index-1)] = currentCell.getNumericCellValue();
                        }
                    }
                    row++;


                    }
                }
                //reset rows and move on to next column
                row = 0;
                index++;
                System.out.println();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //reset for file
        index = 0;
        row = 0;

        try {

            FileInputStream excelFile = new FileInputStream(new File(FILE_TWO));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();

                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();
                    if(index ==0) {
                        /*
                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
                            System.out.print(currentCell.getStringCellValue() + "--");
                        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            System.out.print(currentCell.getNumericCellValue() + "--");
                        }*/
                    }
                    else{
                        if (currentCell.getCellTypeEnum() == CellType.STRING && row == 2) {
                            //System.out.print(currentCell.getStringCellValue() + "--");
                            wordSetTwo[(index-1)] = currentCell.getStringCellValue();

                        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            //System.out.print(currentCell.getNumericCellValue() + "--");
                            if (row == 0) {
                                numSetThree[(index-1)] = currentCell.getNumericCellValue();
                            } else if (row == 1) {
                                numSetFour[(index-1)] = currentCell.getNumericCellValue();
                            }
                        }
                        row++;


                    }
                }
                row = 0;
                index++;
                //System.out.println();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //storing the wanted data by combining them
        for(int i = 0; i < 4;i++)
        {
            finalSet1[i] = numSetOne[i] * numSetThree[i];
            finalSet2[i] = numSetTwo[i] / numSetFour[i];
            finalSet3[i] = wordSetOne[i] + " " +wordSetTwo[i];
        }

        //checking if output is correct
        for(int i = 0; i < 4;i++)
        {
            System.out.print(finalSet1[i]);
            System.out.print(finalSet2[i]);
            System.out.println(finalSet3[i]);
        }



        JSONObject test = new JSONObject();

        try {

            JSONArray numSet1 = new JSONArray();
            JSONArray numSet2 = new JSONArray();
            JSONArray wordSet1 = new JSONArray();


            for(int i = 0; i < 4;i++)
            {
                numSet1.put(finalSet1[i]);
                numSet2.put(finalSet2[i]);
                wordSet1.put(finalSet3[i]);
            }
            test.put("id", "jduong321@gmail.com");
            test.put("numberSetOne",numSet1);
            test.put("numberSetTwo",numSet2);
            test.put("wordSetOne",wordSet1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //does json set up correctly?
        //System.out.println("\n\n"+test);


        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

        try {

            HttpPost request = new HttpPost("http://34.239.125.159:5000/challenge");
            StringEntity params = new StringEntity(test.toString());
            request.setEntity(params);
            request.setHeader("Content-type", "application/json");
            httpClient.execute(request);
            //System.out.println("did stuff");

        }catch (Exception ex) {

            //handle exception here

        } finally {
            //Deprecated
            //httpClient.getConnectionManager().shutdown();
        }

    }

}
