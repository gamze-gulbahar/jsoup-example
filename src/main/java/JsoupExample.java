import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        String productUrl="https://www.vatanbilgisayar.com/dell-vostro-14-5490-core-i7-10510u-1-8ghz-8gb-ram-256gb-ssd-mx250-2gb-14-w10-pro.html";
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

    }

}
