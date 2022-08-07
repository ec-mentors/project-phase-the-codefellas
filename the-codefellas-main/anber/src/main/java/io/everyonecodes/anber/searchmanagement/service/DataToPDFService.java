package io.everyonecodes.anber.searchmanagement.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import io.everyonecodes.anber.providermanagement.data.ContractType;
import io.everyonecodes.anber.providermanagement.data.PriceModelType;
import io.everyonecodes.anber.searchmanagement.data.Provider;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DataToPDFService {

    private final SearchService searchService;
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public DataToPDFService(SearchService searchService) {
        this.searchService = searchService;
    }

    public byte[] sendPDFWithFilters(String filters) throws DocumentException {
        var providerList = searchService.manageFilters(filters);
//        List<Provider> providerList = new ArrayList<>();
//        providerList.add(new Provider("a", "a", 2, ContractType.ONE_MONTH, PriceModelType.FIXED));
//        providerList.add(new Provider("b", "b", 3, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION));
//        providerList.add(new Provider("c", "c", 4, ContractType.FIVE_YEARS, PriceModelType.FIXED));
//        String filepath = "C:\\Users\\Manuel\\Documents\\project-phase-the-codefellas-new\\the-codefellas-main\\anber\\src\\main\\resources\\SearchResult.pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();
        PdfPTable table = new PdfPTable(5);
        addTableHeader(table);
        for(int i = 0; i < providerList.size(); i++) {
            addRows(table, providerList.get(i));
        }
        document.add(table);
        document.close();
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        return pdfBytes;


    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Name of Provider", "Name of Tarriff", "Basic Rate", "Contract Type", "Price Model")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
    private void addRows(PdfPTable table, Provider provider) {
        table.addCell(provider.getProviderName());
        table.addCell(provider.getTariffName());
        table.addCell(String.valueOf(provider.getBasicRate()));
        table.addCell(String.valueOf(provider.getContractType()));
        table.addCell(String.valueOf(provider.getPriceModel()));
    }

    public byte[] sendPDFWithSortByBasicRating(String filters, String operator) throws DocumentException {
        var providerList = searchService.sortByBasicRate(operator, filters);
//        List<Provider> providerList = new ArrayList<>();
//        providerList.add(new Provider("a", "a", 2, ContractType.ONE_MONTH, PriceModelType.FIXED));
//        providerList.add(new Provider("b", "b", 3, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION));
//        providerList.add(new Provider("c", "c", 4, ContractType.FIVE_YEARS, PriceModelType.FIXED));
//        String filepath = "C:\\Users\\Manuel\\Documents\\project-phase-the-codefellas-new\\the-codefellas-main\\anber\\src\\main\\resources\\SearchResult.pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();
        PdfPTable table = new PdfPTable(5);
        addTableHeader(table);
        for(int i = 0; i < providerList.size(); i++) {
            addRows(table, providerList.get(i));
        }
        document.add(table);
        document.close();
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        return pdfBytes;


    }

}
