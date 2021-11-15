package org.nmcpye.activitiesmanagement.extended.web.rest;

import com.google.common.collect.Lists;
import org.nmcpye.activitiesmanagement.domain.sqlview.SqlView;
import org.nmcpye.activitiesmanagement.extended.common.Grid;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessage;
import org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageException;
import org.nmcpye.activitiesmanagement.extended.schemamodule.descriptors.SqlViewSchemaDescriptor;
import org.nmcpye.activitiesmanagement.extended.sqlview.SqlViewQuery;
import org.nmcpye.activitiesmanagement.extended.sqlview.SqlViewService;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.grid.GridUtils;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.CodecUtils;
import org.nmcpye.activitiesmanagement.extended.web.constants.ApiEndPoint;
import org.nmcpye.activitiesmanagement.extended.web.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

import static org.nmcpye.activitiesmanagement.extended.dxf2module.webmessage.WebMessageUtils.*;

@Controller
@RequestMapping(value = ApiEndPoint.API_END_POINT + SqlViewSchemaDescriptor.API_ENDPOINT)
public class SqlViewController
    extends AbstractCrudController<SqlView> {
    @Autowired
    private SqlViewService sqlViewService;

    @Autowired
    private ContextUtils contextUtils;

    // -------------------------------------------------------------------------
    // Get
    // -------------------------------------------------------------------------

    @GetMapping(value = "/{uid}/data", produces = ContextUtils.CONTENT_TYPE_JSON)
    public @ResponseBody
    Grid getViewJson(@PathVariable("uid") String uid,
                     SqlViewQuery query, HttpServletResponse response)
        throws WebMessageException {
        SqlView sqlView = validateView(uid);

//        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_JSON, sqlView.getCacheStrategy() );

        return buildResponse(sqlView, query);
    }

    @GetMapping("/{uid}/data.csv")
    public void getViewCsv(@PathVariable("uid") String uid,
                           @RequestParam(required = false) Set<String> criteria, @RequestParam(required = false) Set<String> var,
                           HttpServletResponse response)
        throws Exception {
        SqlView sqlView = validateView(uid);

        List<String> filters = Lists.newArrayList(contextService.getParameterValues("filter"));
        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));

        Grid grid = sqlViewService.getSqlViewGrid(sqlView, SqlView.getCriteria(criteria), SqlView.getCriteria(var),
            filters, fields);

        String filename = CodecUtils.filenameEncode(grid.getTitle()) + ".csv";

//        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_CSV, sqlView.getCacheStrategy(), filename,
//            true );

        GridUtils.toCsv(grid, response.getWriter());
    }

    @GetMapping("/{uid}/data.xls")
    public void getViewXls(@PathVariable("uid") String uid,
                           @RequestParam(required = false) Set<String> criteria, @RequestParam(required = false) Set<String> var,
                           HttpServletResponse response)
        throws Exception {
        SqlView sqlView = validateView(uid);

        List<String> filters = Lists.newArrayList(contextService.getParameterValues("filter"));
        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));

        Grid grid = sqlViewService.getSqlViewGrid(sqlView, SqlView.getCriteria(criteria), SqlView.getCriteria(var),
            filters, fields);

        String filename = CodecUtils.filenameEncode(grid.getTitle()) + ".xls";

//        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_EXCEL, sqlView.getCacheStrategy(), filename,
//            true );

        GridUtils.toXls(grid, response.getOutputStream());
    }

    @GetMapping("/{uid}/data.html")
    public void getViewHtml(@PathVariable("uid") String uid,
                            @RequestParam(required = false) Set<String> criteria, @RequestParam(required = false) Set<String> var,
                            HttpServletResponse response)
        throws Exception {
        SqlView sqlView = validateView(uid);

        List<String> filters = Lists.newArrayList(contextService.getParameterValues("filter"));
        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));

        Grid grid = sqlViewService
            .getSqlViewGrid(sqlView, SqlView.getCriteria(criteria), SqlView.getCriteria(var), filters, fields);

//        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_HTML, sqlView.getCacheStrategy() );

        GridUtils.toHtml(grid, response.getWriter());
    }

    @GetMapping("/{uid}/data.html+css")
    public void getViewHtmlCss(@PathVariable("uid") String uid,
                               @RequestParam(required = false) Set<String> criteria, @RequestParam(required = false) Set<String> var,
                               HttpServletResponse response)
        throws Exception {
        SqlView sqlView = validateView(uid);

        List<String> filters = Lists.newArrayList(contextService.getParameterValues("filter"));
        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));

        Grid grid = sqlViewService
            .getSqlViewGrid(sqlView, SqlView.getCriteria(criteria), SqlView.getCriteria(var), filters, fields);

//        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_HTML, sqlView.getCacheStrategy() );

        GridUtils.toHtmlCss(grid, response.getWriter());
    }

    @GetMapping("/{uid}/data.pdf")
    public void getViewPdf(@PathVariable("uid") String uid,
                           @RequestParam(required = false) Set<String> criteria, @RequestParam(required = false) Set<String> var,
                           HttpServletResponse response)
        throws Exception {
        SqlView sqlView = validateView(uid);

        List<String> filters = Lists.newArrayList(contextService.getParameterValues("filter"));
        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));

        Grid grid = sqlViewService.getSqlViewGrid(sqlView, SqlView.getCriteria(criteria), SqlView.getCriteria(var),
            filters, fields);

//        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_PDF, sqlView.getCacheStrategy() );

        GridUtils.toPdf(grid, response.getOutputStream());
    }

    // -------------------------------------------------------------------------
    // Post
    // -------------------------------------------------------------------------

    @PostMapping("/{uid}/execute")
    @ResponseBody
    public WebMessage executeView(@PathVariable("uid") String uid,
                                  @RequestParam(required = false) Set<String> var) {
        SqlView sqlView = sqlViewService.getSqlViewByUid(uid);

        if (sqlView == null) {
            return notFound("SQL view does not exist: " + uid);
        }

        if (sqlView.isQuery()) {
            return conflict("SQL view is a query, no view to create");
        }

        String result = sqlViewService.createViewTable(sqlView);
        if (result != null) {
            return conflict(result);
        }
        return created("SQL view created")
            .setLocation(ApiEndPoint.API_END_POINT + SqlViewSchemaDescriptor.API_ENDPOINT + "/" + sqlView.getUid());
    }

    @PostMapping("/{uid}/refresh")
    @ResponseBody
    public WebMessage refreshMaterializedView(@PathVariable("uid") String uid) {
        SqlView sqlView = sqlViewService.getSqlViewByUid(uid);

        if (sqlView == null) {
            return notFound("SQL view does not exist: " + uid);
        }

        if (!sqlViewService.refreshMaterializedView(sqlView)) {
            return conflict("View could not be refreshed");
        }
        return ok("Materialized view refreshed");
    }

    private SqlView validateView(String uid)
        throws WebMessageException {
        SqlView sqlView = sqlViewService.getSqlViewByUid(uid);

        if (sqlView == null) {
            throw new WebMessageException(notFound("SQL view does not exist: " + uid));
        }

        return sqlView;
    }

    private Grid buildResponse(SqlView sqlView, SqlViewQuery query)
        throws WebMessageException {
        List<String> filters = Lists.newArrayList(contextService.getParameterValues("filter"));
        List<String> fields = Lists.newArrayList(contextService.getParameterValues("fields"));

        Grid grid = sqlViewService.getSqlViewGrid(sqlView, SqlView.getCriteria(query.getCriteria()),
            SqlView.getCriteria(query.getVar()), filters, fields);

        if (!query.isSkipPaging()) {
            query.setTotal(grid.getHeight());
            grid.limitGrid((query.getPage() - 1) * query.getPageSize(),
                Integer.min(query.getPage() * query.getPageSize(), grid.getHeight()));
//            rootNode.addChild( NodeUtils.createPager( query.getPager() ) );
        }

        return grid;
    }
}
