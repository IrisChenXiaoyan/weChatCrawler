package top.kittygirl.wechat;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
//将WebCollector示例程序修改为获取西电新闻网
public class NewsCrawler extends BreadthCrawler {
    /**
     * @param crawlPath crawlPath is the path of the directory which maintains
     *                  information of this crawler
     * @param autoParse if autoParse is true,BreadthCrawler will auto extract
     *                  links which match regex rules from pag
     */
    public NewsCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        /*start page*/
        this.addSeed("http://news.xidian.edu.cn/");

        /*fetch url like http://news.hfut.edu.cn/show-xxxxxxhtml*/
        this.addRegex("http://news.xidian.edu.cn/info/.*htm");
        /*do not fetch jpg|png|gif*/
        this.addRegex("-.*\\.(jpg|png|gif).*");
        /*do not fetch url contains #*/
        this.addRegex("-.*#.*");

        setThreads(50);
        getConf().setTopN(100);

    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        String url = page.url();
        /*if page is news page*/
        if (page.matchUrl("http://news.xidian.edu.cn/info/.*htm")) {

            /*extract title and content of news by css selector*/
            String title = page.select("div.neirong-bt").text();
            //String date = page.select("span#date").text();
            //String clickNum = page.select("div#wz_info.b_b").first().child(3).select("span").first().child(0).select("span").val();
            //String content = page.selectText("div#artibody");

            System.out.println("URL:\n" + url);
            System.out.println("title:\n" + title);
            //System.out.println("date:\n" + date);
            //System.out.println("clickNum:\n" + clickNum);
            //System.out.println("content:\n" + content);

            /*If you want to add urls to crawl,add them to nextLink*/
            /*WebCollector automatically filters links that have been fetched before*/
            /*If autoParse is true and the link you add to nextLinks does not match the
              regex rules,the link will also been filtered.*/
            //next.add("http://xxxxxx.com");
        }
    }

    public static void main(String[] args) throws Exception {
        NewsCrawler crawler = new NewsCrawler("crawl", true);
        /*start crawl with depth of 4*/
        crawler.start(3);
    }

}