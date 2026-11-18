# data.gov.in Mandi Price Importer

This update adds a scheduled/import-on-startup integration for the verified data.gov.in resource `9ef84268-d588-465a-a308-a864a43d0070`.

## Important safety defaults
- Import is disabled by default (`mandi.import.enabled: false`).
- One-time startup import is disabled by default (`mandi.import.run-on-startup: false`).
- Initial district scope is Sangli; set `mandi.import.district:` blank to import all Maharashtra records.
- The page size is 10 because that is the page size verified in Postman. The importer uses the API's `total` and returned record count to paginate.
- Only commodities matching an existing active crop are imported. Unknown commodities are skipped and counted; no crop rows are invented because `crops.name_mr` is mandatory.
- Mandi rows are matched by market name + district + state; missing mandi rows are created with required English fields.
- Existing imported price rows are updated by a deterministic SHA-256 source key, so a later revised daily price replaces the previous value for that same market/commodity/variety/grade/date.
- `arrivals` remains null because this API response does not supply an arrivals field.
- `ddl-auto: validate` remains unchanged. No database tables are created or dropped by this code.

## Configure IntelliJ environment variables
Run > Edit Configurations > MandiTrackerApiApplication > Environment variables:

- `DB_PASSWORD` = your PostgreSQL password
- `DATA_GOV_IN_API_KEY` = your newly regenerated data.gov.in API key

Do not commit either value to Git.

## First one-time Sangli import
1. Keep `mandi.import.district: Sangli`.
2. Set `mandi.import.run-on-startup: true`.
3. Keep `mandi.import.enabled: false`.
4. Run the Spring Boot application once and inspect the console summary.
5. After confirming database rows, set `run-on-startup: false` again.

## Enable daily scheduled imports later
Set `mandi.import.enabled: true` and `mandi.import.run-on-startup: false`. Default schedule is 16:00 Asia/Kolkata. Adjust `mandi.import.cron` as needed. Set district blank for all Maharashtra.

## Verify imported rows
```sql
SELECT mp.id, mp.commodity_name, m.name AS market, m.district,
       mp.variety, mp.grade, mp.price_date,
       mp.min_price, mp.modal_price, mp.max_price,
       mp.source, mp.source_record_id
FROM mandi_prices mp
JOIN mandis m ON m.id = mp.mandi_id
ORDER BY mp.price_date DESC, m.district, m.name, mp.commodity_name;
```

## Database note
The provided schema export contains column metadata, not index/constraint definitions. `source_record_id` lookup prevents normal repeat imports in a single application instance, but a unique database constraint should only be added after checking existing duplicates and the current indexes/constraints. This code intentionally does not alter the schema.
