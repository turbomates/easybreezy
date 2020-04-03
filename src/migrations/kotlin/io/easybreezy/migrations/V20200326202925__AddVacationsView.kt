package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200326202925__AddVacationsView : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            create view vacations as SELECT
            ul.user_id as user_id,
            sum(wh.count) as absence_hours,
            (SELECT count(*) /*absence days without holidays*/
                FROM absences a
                LEFT JOIN generate_series(a.started_at::date, a.ended_at::date, interval '1 day') a_day on true
                WHERE a.user_id = ul.user_id and extract('ISODOW' FROM a_day) < 6 and a.started_at >= ul.started_at and a.ended_at <= ul.ended_at
            )
            -
            (SELECT count(*) FROM holidays h /* holidays on working days */
                LEFT JOIN calendars c on h.calendar = c.id and l.id = c.location
                LEFT JOIN absences a on a.user_id = ul.user_id
                WHERE extract('ISODOW' FROM h.day) < 6 and h.day between a.started_at and a.ended_at and h.is_working_day = false
                    and a.started_at >= ul.started_at and a.ended_at <= ul.ended_at)
            +
            (SELECT count(*) FROM holidays h /* missed working days */
                LEFT JOIN calendars c on h.calendar = c.id and l.id = c.location
                LEFT JOIN absences a on a.user_id = ul.user_id
                WHERE h.day between a.started_at and a.ended_at and h.is_working_day = true and a.started_at >= ul.started_at and a.ended_at <= ul.ended_at)
            as absence_days,
            ul.started_at as location_started_at,
            ul.ended_at as location_ended_at,
            ul.extra_vacation_days as extra_vacation_days,
            l.vacation_days as vacation_days
            FROM user_locations ul
            INNER JOIN locations l on ul.location = l.id
            LEFT JOIN working_hours wh on ul.user_id = wh.user_id and wh.day between ul.started_at and ul.ended_at
        --     WHERE wh.day between ul.started_at and ul.ended_at
            GROUP BY ul.user_id, l.id, extra_vacation_days, vacation_days, location_started_at, location_ended_at;
        """.trimIndent())
    }
}
