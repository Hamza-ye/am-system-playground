package org.nmcpye.activitiesmanagement.extended.resourcetable.jdbc;

import org.nmcpye.activitiesmanagement.extended.resourcetable.ResourceTable;
import org.nmcpye.activitiesmanagement.extended.resourcetable.ResourceTableStore;
import org.nmcpye.activitiesmanagement.extended.system.util.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service("org.nmcpye.activitiesmanagement.extended.resourcetable.ResourceTableStore")
public class JdbcResourceTableStore implements ResourceTableStore {

    private final Logger log = LoggerFactory.getLogger(JdbcResourceTableStore.class);
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final JdbcTemplate jdbcTemplate;

    public JdbcResourceTableStore(JdbcTemplate jdbcTemplate) {
        checkNotNull(jdbcTemplate);

        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // ResourceTableStore implementation
    // -------------------------------------------------------------------------

    @Override
    public void generateResourceTable(ResourceTable<?> resourceTable) {
        log.info(String.format("Generating resource table: '%s'", resourceTable.getTableName()));

        final Clock clock = new Clock().startClock();
        final String createTableSql = resourceTable.getCreateTempTableStatement();
        final Optional<String> populateTableSql = resourceTable.getPopulateTempTableStatement();
        final Optional<List<Object[]>> populateTableContent = resourceTable.getPopulateTempTableContent();
        final List<String> createIndexSql = resourceTable.getCreateIndexStatements();
        //        final String analyzeTableSql = statementBuilder.getAnalyze( resourceTable.getTableName() );

        // ---------------------------------------------------------------------
        // Drop temporary table if it exists
        // ---------------------------------------------------------------------

        jdbcTemplate.execute(resourceTable.getDropTempTableStatement());

        // ---------------------------------------------------------------------
        // Create temporary table
        // ---------------------------------------------------------------------

        log.debug(String.format("Create table SQL: '%s'", createTableSql));

        jdbcTemplate.execute(createTableSql);

        // ---------------------------------------------------------------------
        // Populate temporary table through SQL or object batch update
        // ---------------------------------------------------------------------

        if (populateTableSql.isPresent()) {
            log.debug(String.format("Populate table SQL: '%s'", populateTableSql.get()));

            jdbcTemplate.execute(populateTableSql.get());
        } else if (populateTableContent.isPresent()) {
            List<Object[]> content = populateTableContent.get();

            log.debug(String.format("Populate table content rows: '%d'", content.size()));

            if (content.size() > 0) {
                int columns = content.get(0).length;

                batchUpdate(columns, resourceTable.getTempTableName(), content);
            }
        }

        // ---------------------------------------------------------------------
        // Invoke hooks
        // ---------------------------------------------------------------------

        //        List<AnalyticsTableHook> hooks = analyticsTableHookService
        //            .getByPhaseAndResourceTableType(AnalyticsTablePhase.RESOURCE_TABLE_POPULATED, resourceTable.getTableType());
        //
        //        if (!hooks.isEmpty()) {
        //            analyticsTableHookService.executeAnalyticsTableSqlHooks(hooks);
        //
        //            log.info(String.format("Invoked resource table hooks: '%d'", hooks.size()));
        //        }

        // ---------------------------------------------------------------------
        // Create indexes
        // ---------------------------------------------------------------------

        for (final String sql : createIndexSql) {
            log.debug(String.format("Create index SQL: '%s'", sql));

            jdbcTemplate.execute(sql);
        }

        // ---------------------------------------------------------------------
        // Swap tables
        // ---------------------------------------------------------------------

        jdbcTemplate.execute(resourceTable.getDropTableStatement());

        jdbcTemplate.execute(resourceTable.getRenameTempTableStatement());

        log.debug(String.format("Swapped resource table: '%s'", resourceTable.getTableName()));

        // ---------------------------------------------------------------------
        // Analyze
        // ---------------------------------------------------------------------

        //        if (analyzeTableSql != null) {
        //            log.debug("Analyze table SQL: " + analyzeTableSql);
        //
        //            jdbcTemplate.execute(analyzeTableSql);
        //        }

        log.debug(String.format("Analyzed resource table: '%s'", resourceTable.getTableName()));

        log.info(String.format("Resource table '%s' update done: '%s'", resourceTable.getTableName(), clock.time()));
    }

    @Override
    public void batchUpdate(int columns, String tableName, List<Object[]> batchArgs) {
        if (columns == 0 || tableName == null) {
            return;
        }

        StringBuilder builder = new StringBuilder("insert into " + tableName + " values (");

        for (int i = 0; i < columns; i++) {
            builder.append("?,");
        }

        builder.deleteCharAt(builder.length() - 1).append(")");

        jdbcTemplate.batchUpdate(builder.toString(), batchArgs);
    }
}
