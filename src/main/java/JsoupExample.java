import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.ws.Response;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class JsoupExample {
    static Set<String> productImagesWithPagination = new HashSet<String>();

    static Document loadDocumentFromURL(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc;
    }

    static Document getDocUserAgent(String url) throws IOException {
        Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Linux; <Android Version>; <Build Tag etc.>) " +
                "AppleWebKit/<WebKit Rev> (KHTML, like Gecko) " +
                "Chrome/<Chrome Rev> Mobile Safari/<WebKit Rev>").get();
        File file = new File("/Users/gamze/IdeaProjects/JsoupExample/mobile.html");

        if (file.createNewFile()){
            FileWriter writer = new FileWriter(file);
            writer.write(doc.toString());
            writer.close();
        } else {
            FileWriter writer = new FileWriter(file);
            writer.write(doc.toString());
            writer.close();
        }
        return doc;
    }
    static void getTitle(String url) throws IOException {
        Document document = loadDocumentFromURL(url);
        String title = document.title();
        System.out.println(title);
    }
    static void getTitleWithHeader(String url) throws IOException {

        Document document = Jsoup.connect(url)
                .header("cookie", "cookie: userProfile=options=SXdyy0yVGsaIP/ADFILXrWOc5B+5SfHg5cmf8nhBLysCAHWcNRpGdzCXzQ7oo0M4qnb/CA==; bannerLocalSite=show=GqDURKWa/75Xi4/akQbag28CnPlfTMcvJREK0kWSklyu1MgfkXHTQEU1l180u7ro7f1e2A==; _gcl_au=1.1.37765046.1590756032; _fbp=fb.1.1590756032259.1577750378; _hjid=6281bc21-2fc1-40b0-83eb-cf51495f1c2e; _hjIncludedInSample=1; cookiesConsent=firstParty%3Dtrue%26thirdParty%3Dtrue%26showBanner%3Dfalse; _ga=GA1.2.1573003090.1590756044; _gid=GA1.2.178953329.1590756044; wcsid=gcDlGzPM8wmBsVEe9791Q0MaE4L3bAAb; hblid=IMK9A5DmiGcDjN4M9791Q0MbAL43boFE; _okdetect=%7B%22token%22%3A%2215907560444550%22%2C%22proto%22%3A%22https%3A%22%2C%22host%22%3A%[22www.lordgunbicycles.es](http://22www.lordgunbicycles.es/)%22%7D; olfsk=olfsk7252750383664082; _ok=3897-260-10-9699; _okac=e5b37753bc938aa87a1c862bc37a23fd; _okla=1; _okbk=cd5%3Davailable%2Ccd4%3Dtrue%2Cvi5%3D0%2Cvi4%3D1590756044798%2Cvi3%3Dactive%2Cvi2%3Dfalse%2Cvi1%3Dfalse%2Ccd8%3Dchat%2Ccd6%3D0%2Ccd3%3Dfalse%2Ccd2%3D0%2Ccd1%3D0%2C; __RequestVerificationToken=TVZMVmnl18n3xETCTTgfBrwfc-cjZRDeG3SrSR7zzfkIPDSUBm5Y8ZHaMG2VgHT_UU-wEfRwj93bBUSi4RTCy_qH1n01; _oklv=1590760226418%2CgcDlGzPM8wmBsVEe9791Q0MaE4L3bAAb")
                .get();
        String title = document.title();
        System.out.println(title);
    }

    static void getOneImage(Document doc){
        //class="wrapper-main-slider__wrapper-image"
        Element pDiv = doc.select("div.wrapper-main-slider__wrapper-image").first();
        Element imgSrc = pDiv.selectFirst("img[src$=.jpg]");
        String link = imgSrc.attr("abs:src");
        System.out.println("Single: " + link);
        System.out.println("---------------------------------");
    }
    static ArrayList<String> getAllLinksInOnePage(Document doc){
        //System.out.println(doc);
        ArrayList<String> productImages = new ArrayList<String>();
        //class="product-list product-list--list-page"
        Elements productList = doc.select(".product-list.product-list--list-page " +
                ".product-list__image-safe " +
                ".product-list__image-safe-link " +
                ".slider-img .lazyimg");
        for (Element productLink : productList){
            String link = productLink.attr("abs:data-src");
            productImages.add(link);
            productImagesWithPagination.add(link);
        }
        return productImages;
    }

    static void getAllLinksInOnePageWithPagination(String url) throws IOException {

        for(int i =1; i<10; i++){
            String updatedUrl = url + "?page=" + i;
            Document doc = loadDocumentFromURL(updatedUrl);
            ArrayList<String> productImages = getAllLinksInOnePage(doc);
            if (productImages.isEmpty() == true) break;
        }

        int i =1;
        for (String product : productImagesWithPagination) {
            System.out.println("Product"+ i +": " + product);
            i++;
        }

    }
    public static void main (String[]args) throws IOException {
        String productUrl="https://www.vatanbilgisayar.com/dell-vostro" +
                "-14-5490-core-i7-10510u-1-8ghz-8gb-ram-256gb-ssd-mx250-2gb-14-w10-pro.html";
        Document doc = loadDocumentFromURL(productUrl);
        getOneImage(doc);

        String productsUrl = "https://www.vatanbilgisayar.com/lenovo/bilgisayar/";
        Document docWithProducts = loadDocumentFromURL(productsUrl);

        ArrayList<String> productImages = new ArrayList<String>();
        productImages = getAllLinksInOnePage(docWithProducts);

        for (int i =1; i<productImages.size(); i++) {
            System.out.println("Product"+ i + ": " +productImages.get(i));
        }

        System.out.println("---------------------------------");
        getAllLinksInOnePageWithPagination(productsUrl);
        getDocUserAgent(productUrl);

        String newUrl = "https://www.lordgunbicycles.co.uk/camelbak-crux-reservoir" +
                "-hydration-bladder?option=21939&gclid=CO6ojo2CsNMCFcsK0wodzYwNIQ";
        getTitle(newUrl);
        String uurl = "https://www.lordgunbicycles.es/bolsa-de-agua-camelbak-crux-reservoir";
        getTitleWithHeader(newUrl);
    }

}
