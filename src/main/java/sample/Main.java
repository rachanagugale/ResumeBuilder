package sample;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main extends Application {

    String name, college, email, address;
    int collegePassingYear;
    float collegeCGPA;
    long phone;
    ProcessData processData;

    @Override
    public void start(Stage primaryStage) throws Exception{

        GridPane root = new GridPane();
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setOrientation(Orientation.VERTICAL);

        Label nameLabel = new Label("Name : ");
        Label collegeLabel = new Label("College: ");
        Label addressLabel = new Label("Address : ");
        Label emailLabel = new Label("Email: ");
        Label CGPALabel = new Label("CGPA : ");
        Label phoneLabel = new Label("Phone: ");
        Label collegePassingYearLabel = new Label("College Passing Year : ");
        Label personalInfoLabel = new Label("Personal Info ");


        TextField nameTF = new TextField();
        TextField collegeTF = new TextField();
        TextField addressTF = new TextField();
        TextField emailTF = new TextField();
        TextField cgpaTF = new TextField();
        TextField phoneTF = new TextField();
        TextField collegePassingYearTF = new TextField();

        javafx.scene.control.Button submit = new Button("Submit");

        submit.setOnAction(actionEvent ->
        {
            name = nameTF.getText();
            college = collegeTF.getText();
            email = emailTF.getText();
            address = addressTF.getText();
            collegeCGPA = Float.parseFloat(cgpaTF.getText());
            collegePassingYear = Integer.parseInt(collegePassingYearTF.getText());

            processData = new ProcessData(name, college, email, phone, address, collegePassingYear, collegeCGPA);
            try
            {
                processData.process();
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (DocumentException e)
            {
                e.printStackTrace();
            }

        });

        root.addRow(0,nameLabel,nameTF);
        root.addRow(1,collegeLabel,collegeTF);
        root.addRow(2,collegePassingYearLabel,collegePassingYearTF);
        root.addRow(3,CGPALabel,cgpaTF);
        root.addRow(4,personalInfoLabel);
        root.addRow(5,addressLabel,addressTF);
        root.addRow(6,emailLabel,emailTF);
        root.addRow(7,phoneLabel,phoneTF);
        root.addRow(8,submit);

        Scene scene = new Scene(root,400,200);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

class ProcessData
{
    String name;
    String college;
    int collegePassingYear;
    float collegeCGPA;
    String email;
    long phone;
    String address;


    ProcessData(String name, String college, String email, long phone, String address, int collegePassingYear, float collegeCGPA)
    {
        this.name = name;
        this.college = college;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.collegePassingYear = collegePassingYear;
        this.collegeCGPA = collegeCGPA;
    }

    void process() throws IOException, DocumentException
    {
        File htmlTemplateFile = new File("C:\\Users\\Rachana\\Desktop\\Programs\\Java_Programs\\STQA\\ResumeBuilder\\src\\main\\java\\sample\\resumeTemplate.html");
        String htmlString = FileUtils.readFileToString(htmlTemplateFile);


        htmlString = htmlString.replace("$name", name);
        htmlString = htmlString.replace("$college", college);

        htmlString = htmlString.replace("$CGPA", Float.toString(collegeCGPA));
        htmlString = htmlString.replace("$year", Integer.toString(collegePassingYear));
        htmlString = htmlString.replace("$phone", Long.toString(phone));

        String personalInfo = "<h2>Personal Info</h2><hr></hr>" + "<h4>Address</h4>" + address + "<h4>Phone</h4>" + phone + "<h4>Email</h4>" + email ;
        htmlString = htmlString.replace("<h2>Personal Info</h2>", personalInfo);
        File newHtmlFile = new File("C:\\Users\\Rachana\\Desktop\\Programs\\Java_Programs\\STQA\\ResumeBuilder\\src\\main\\java\\sample\\resume.html");
        FileUtils.writeStringToFile(newHtmlFile, htmlString);


        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream("C:\\Users\\Rachana\\Desktop\\Programs\\Java_Programs\\STQA\\ResumeBuilder\\src\\main\\java\\sample\\output.pdf"));
        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new FileInputStream(newHtmlFile));
        document.close();
    }
}
