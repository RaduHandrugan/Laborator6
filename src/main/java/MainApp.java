import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MainApp
{

    private static final String FILE_PATH = "D:\\Apps\\IntelliJ\\Laborator6\\src\\main\\java\\angajati.json";  //imi declar locatia (path) pt fisierul angajati

    public static void main(String[] args) throws IOException
    {
        List<Angajat> angajati = citire();

        //1
        angajati.forEach(System.out::println);

        //2. angajati cu salariu peste 2500 RON
        angajati.stream().filter(a -> a.getSalariul() > 2500).forEach(System.out::println);

        //3.angajați din aprilie anul trecut cu funcție de conducere
        int anulCurent = LocalDate.now().getYear();
        List<Angajat> angajatiConducere = angajati.stream().filter(a -> a.getPostul().toLowerCase().contains("sef") || a.getPostul().toLowerCase().contains("director")).filter(a -> a.getData_angajarii().getYear() == anulCurent - 1 && a.getData_angajarii().getMonthValue() == 4).collect(Collectors.toList());
        angajatiConducere.forEach(System.out::println);

        //4. ang fara conducere ordonati
        angajati.stream().filter(a -> !(a.getPostul().toLowerCase().contains("sef") || a.getPostul().toLowerCase().contains("director"))).sorted(Comparator.comparingDouble(Angajat::getSalariul).reversed()).forEach(System.out::println);

        //5. numele cu majuscule
        angajati.stream().map(a -> a.getNume().toUpperCase()).collect(Collectors.toList()).forEach(System.out::println);

        //6. salarii < 3000
        angajati.stream().map(Angajat::getSalariul).filter(s -> s < 3000).forEach(System.out::println);

        //7. prima data de angajare
        Optional<Angajat> angajatMin = angajati.stream().min(Comparator.comparing(Angajat::getData_angajarii));
        angajatMin.ifPresentOrElse(System.out::println, () -> System.out.println("Nu exista angajați! ! !"));

        //8. statistica
        DoubleSummaryStatistics stats = angajati.stream().collect(Collectors.summarizingDouble(Angajat::getSalariul));
        System.out.println("Salariul mediu: " + stats.getAverage());
        System.out.println("Salariul minim: " + stats.getMin());
        System.out.println("Salariul maxim: " + stats.getMax());

        //9. cautare nume
        Optional<Angajat> ion = angajati.stream().filter(a -> a.getNume().toLowerCase().contains("ion")).findAny();
        ion.ifPresentOrElse(
                a -> System.out.println("Firma are cel putin un angajat cu numele Ion"), () -> System.out.println("Firma nu are nici un angajat cu numele Ion")
        );

        //10. nr angajati in anul precedent, vara
        long angajatiVara = angajati.stream().filter(a -> a.getData_angajarii().getYear() == anulCurent - 1).filter(a -> a.getData_angajarii().getMonthValue() >= 6 && a.getData_angajarii().getMonthValue() <= 8).count();
        System.out.println("Nr angajați in vara anului trecut: " + angajatiVara);
    }

    public static List<Angajat> citire() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        File file = new File(FILE_PATH);
        return Arrays.asList(mapper.readValue(file, Angajat[].class));
    }

    public static void scriere(List<Angajat> angajati) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        File file = new File(FILE_PATH);
        mapper.writeValue(file, angajati);
    }
}
