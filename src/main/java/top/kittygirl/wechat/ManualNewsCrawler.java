package top.kittygirl.wechat;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

//手动解析新URL
public class ManualNewsCrawler extends BreadthCrawler {
    /**
     * @param crawlPath crawlPath is the path of the directory which maintains
     *                  information of this crawler
     * @param autoParse if autoParse is true,BreadthCrawler will auto extract
     *                  links which match regex rules from pag
     */
    public ManualNewsCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);

        this.addSeed("http://news.xidian.edu.cn/info/1399/198630.htm", "list");
        this.addSeed("http://news.xidian.edu.cn/info/2106/198635.htm", "list");

        setThreads(50);
        getConf().setTopN(100);

    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        String url = page.url();

        if (page.matchType("list")) {
            /*if type is "list"*/
            /*detect content page by css selector and mark their types as "content"*/
            next.add(page.links("div[class=' m-juhe-nav '] li>a")).type("content");
        }else if(page.matchType("content")) {
            /*if type is "content"*/
            /*extract title and content of news by css selector*/
            String title = page.select("div.neirong-bt").text();
            //read title_prefix and content_length_limit from configuration
            title = getConf().getString("title_prefix") + title;

            System.out.println("URL:\n" + url);
            System.out.println("title:\n" + title);

        }

    }

    public static void main(String[] args) throws Exception {
        ManualNewsCrawler crawler = new ManualNewsCrawler("crawl", false);

        crawler.getConf().setExecuteInterval(5000);

        crawler.getConf().set("title_prefix","PREFIX_");
        crawler.getConf().set("content_length_limit", 20);

        /*start crawl with depth of 4*/
        crawler.start(4);
    }

}
