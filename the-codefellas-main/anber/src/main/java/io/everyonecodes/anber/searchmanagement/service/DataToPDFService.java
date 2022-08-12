package io.everyonecodes.anber.searchmanagement.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import io.everyonecodes.anber.searchmanagement.data.Provider;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
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

        Document document = new Document();
        document.setPageSize(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        PdfPTable table = new PdfPTable(6);
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
        Stream.of("Provider-ID", "Name of Provider", "Name of Tarriff", "Basic Rate", "Contract Type", "Price Model")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
    private void addRows(PdfPTable table, Provider provider) {
        table.addCell(String.valueOf(provider.getId()));
        table.addCell(provider.getProviderName());
        table.addCell(provider.getTariffName());
        table.addCell(String.valueOf(provider.getBasicRate()));
        table.addCell(String.valueOf(provider.getContractType()));
        table.addCell(String.valueOf(provider.getPriceModel()));
    }

    public byte[] sendPDFWithSortByBasicRating(String filters, String operator) throws DocumentException {
        var providerList = searchService.sortByBasicRate(operator, filters);

        Document document = new Document();
        document.setPageSize(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        PdfPTable table = new PdfPTable(6);
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
