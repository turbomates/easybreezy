package io.easybreezy.hr.api

import com.google.inject.Inject
import io.easybreezy.hr.api.controller.AbsenceController
import io.easybreezy.hr.api.controller.CalendarController
import io.easybreezy.hr.api.controller.HRController
import io.easybreezy.hr.api.controller.LocationController
import io.easybreezy.hr.api.controller.VacationController
import io.easybreezy.hr.application.absence.CreateAbsence
import io.easybreezy.hr.application.absence.EditWorkingHours
import io.easybreezy.hr.application.absence.NoteWorkingHours
import io.easybreezy.hr.application.absence.RemoveWorkingHours
import io.easybreezy.hr.application.absence.UpdateAbsence
import io.easybreezy.hr.application.absence.queryobject.Absence
import io.easybreezy.hr.application.absence.queryobject.Absences
import io.easybreezy.hr.application.absence.queryobject.UserAbsences
import io.easybreezy.hr.application.absence.queryobject.UserWorkingHours
import io.easybreezy.hr.application.absence.queryobject.WorkingHour
import io.easybreezy.hr.application.absence.queryobject.WorkingHours
import io.easybreezy.hr.application.calendar.command.AddHoliday
import io.easybreezy.hr.application.calendar.command.EditCalendar
import io.easybreezy.hr.application.calendar.command.EditHoliday
import io.easybreezy.hr.application.calendar.command.ImportCalendar
import io.easybreezy.hr.application.calendar.command.RemoveHoliday
import io.easybreezy.hr.application.calendar.queryobject.Calendars
import io.easybreezy.hr.application.calendar.queryobject.Holidays
import io.easybreezy.hr.application.hr.command.ApplyPosition
import io.easybreezy.hr.application.hr.command.ApplySalary
import io.easybreezy.hr.application.hr.command.Fire
import io.easybreezy.hr.application.hr.command.Hire
import io.easybreezy.hr.application.hr.command.SpecifySkills
import io.easybreezy.hr.application.hr.command.UpdateBio
import io.easybreezy.hr.application.hr.command.UpdateBirthday
import io.easybreezy.hr.application.hr.command.WriteNote
import io.easybreezy.hr.application.hr.queryobject.Employee
import io.easybreezy.hr.application.hr.queryobject.EmployeeDetails
import io.easybreezy.hr.application.location.AssignLocation
import io.easybreezy.hr.application.location.CreateLocation
import io.easybreezy.hr.application.location.EditUserLocation
import io.easybreezy.hr.application.location.queryobject.Locations
import io.easybreezy.hr.application.location.queryobject.UserLocation
import io.easybreezy.hr.application.location.queryobject.UserLocations
import io.easybreezy.hr.application.RemainingTime
import io.easybreezy.hr.application.RemainingTimes
import io.easybreezy.infrastructure.ktor.EmptyParams
import io.easybreezy.infrastructure.ktor.GenericPipeline
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.ktor.Router
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.infrastructure.ktor.delete
import io.easybreezy.infrastructure.ktor.deleteWithBody
import io.easybreezy.infrastructure.ktor.get
import io.easybreezy.infrastructure.ktor.post
import io.ktor.application.Application
import io.ktor.auth.authenticate
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.routing
import java.util.UUID

class Router @Inject constructor(
    application: Application,
    genericPipeline: GenericPipeline
) : Router(application, genericPipeline) {

    init {
        application.routing {
            route("/api/hr") {
                authenticate(*Auth.user) {
                    absencesRouting(this)
                    locationsRouting(this)
                    hrRouting(this)
                    calendarsRouting(this)
                    vacationRouting(this)
                }
            }
        }
    }

    private fun absencesRouting(route: Route) {
        route.route("/absences") {
            workingHoursRouting(this)

            post<Response.Either<Response.Ok, Response.Errors>, CreateAbsence>("") { command ->
                controller<AbsenceController>(this).createAbsence(
                    command
                )
            }
            post<Response.Either<Response.Ok, Response.Errors>, UpdateAbsence, ID>("/{id}") { command, params ->
                controller<AbsenceController>(this).updateAbsence(
                    params.id,
                    command
                )
            }
            delete<Response.Ok, ID>("/{id}") { params ->
                controller<AbsenceController>(this).removeAbsence(
                    params.id
                )
            }
            get<Response.Data<Absence>, ID>("/{id}") { params ->
                controller<AbsenceController>(this).showAbsence(
                    params.id
                )
            }
            get<Response.Data<UserAbsences>>("/me") { controller<AbsenceController>(this).myAbsences(resolvePrincipal<UserPrincipal>()) }
            get<Response.Data<Absences>>("") { controller<AbsenceController>(this).absences() }
        }
    }

    private fun workingHoursRouting(route: Route) {
        route.route("/working-hours") {
            post<Response.Ok, NoteWorkingHours>("") { command ->
                controller<AbsenceController>(this).noteWorkingHours(
                    command
                )
            }
            post<Response.Ok, EditWorkingHours>("/update") { command ->
                controller<AbsenceController>(this).editWorkingHours(
                    command
                )
            }
            delete<Response.Ok, EmptyParams, EmptyParams, RemoveWorkingHours>("") { _, _, command ->
                controller<AbsenceController>(this).removeWorkingHours(
                    command
                )
            }
            get<Response.Data<WorkingHour>, ID>("/{id}") { routeParams ->
                controller<AbsenceController>(this).showWorkingHour(routeParams.id)
            }
            get<Response.Data<UserWorkingHours>>("/me") {
                controller<AbsenceController>(this).myWorkingHours(
                    resolvePrincipal<UserPrincipal>()
                )
            }
            get<Response.Data<WorkingHours>>("") { controller<AbsenceController>(this).workingHours() }
        }
    }

    private fun locationsRouting(route: Route) {
        route.route("/locations") {
            post<Response.Either<Response.Ok, Response.Errors>, CreateLocation>("") { command ->
                controller<LocationController>(this).createLocation(command)
            }
            delete<Response.Ok, ID>("/{id}") { params -> controller<LocationController>(this).removeLocation(params.id) }
            get<Response.Data<Locations>>("") {
                controller<LocationController>(this).locations()
            }
            route("/user") {
                post<Response.Either<Response.Ok, Response.Errors>, AssignLocation>("") { command ->
                    controller<LocationController>(this).assignLocation(
                        command
                    )
                }
                post<Response.Either<Response.Ok, Response.Errors>, EditUserLocation, ID>("/{id}") { command, params ->
                    controller<LocationController>(this).editUserLocation(
                        params.id,
                        command
                    )
                }
                delete<Response.Ok, ID>("/{id}") { params ->
                    controller<LocationController>(this).removeUserLocation(
                        params.id
                    )
                }
                get<Response.Data<UserLocation>, ID>("/{id}") { params ->
                    controller<LocationController>(this).showUserLocation(params.id)
                }
                get<Response.Data<UserLocations>>("") {
                    controller<LocationController>(this).userLocations()
                }
            }
        }
    }

    private fun hrRouting(route: Route) {
        route.route("/employee/{id}") {
            route.route("/employees") {
                get<Response.Listing<Employee>>("") {
                    controller<HRController>(this).employees()
                }
            }

            route.route("/employee/{userId}") {
                get<Response.Data<EmployeeDetails>, UserId>("") { params ->
                    controller<HRController>(this).employee(params.userId)
                }

                post<Response.Either<Response.Ok, Response.Errors>, Hire, UserId>("/hire") { command, params ->
                    controller<HRController>(this).hire(command, params.userId, resolvePrincipal<UserPrincipal>())
                }

                post<Response.Either<Response.Ok, Response.Errors>, Fire, UserId>("/fire") { command, params ->
                    controller<HRController>(this).fire(command, params.userId, resolvePrincipal<UserPrincipal>())
                }

                post<Response.Either<Response.Ok, Response.Errors>, WriteNote, UserId>("/write-note") { command, params ->
                    controller<HRController>(this).writeNote(command, params.userId, resolvePrincipal<UserPrincipal>())
                }

                post<Response.Either<Response.Ok, Response.Errors>, SpecifySkills, UserId>("/specify-skills") { command, params ->
                    controller<HRController>(this).specifySkills(command, params.userId)
                }

                post<Response.Either<Response.Ok, Response.Errors>, UpdateBio, UserId>("/update-bio") { command, params ->
                    controller<HRController>(this).updateBio(command, params.userId)
                }

                post<Response.Either<Response.Ok, Response.Errors>, UpdateBirthday, UserId>("/update-birthday") { command, params ->
                    controller<HRController>(this).updateBirthday(command, params.userId)
                }

                post<Response.Either<Response.Ok, Response.Errors>, ApplyPosition, UserId>("/apply-position") { command, params ->
                    controller<HRController>(this).applyPosition(
                        command,
                        params.userId,
                        resolvePrincipal<UserPrincipal>()
                    )
                }

                post<Response.Either<Response.Ok, Response.Errors>, ApplySalary, UserId>("/apply-salary") { command, params ->
                    controller<HRController>(this).applySalary(
                        command,
                        params.userId,
                        resolvePrincipal<UserPrincipal>()
                    )
                }
            }
        }
    }

    private fun calendarsRouting(route: Route) {
        route.route("/calendars") {
            holidaysRouting(this)

            post<Response.Either<Response.Ok, Response.Errors>, ImportCalendar>("") { command ->
                controller<CalendarController>(this).importCalendar(command)
            }
            post<Response.Either<Response.Ok, Response.Errors>, EditCalendar, ID>("/{id}") { command, params ->
                controller<CalendarController>(this).editCalendar(params.id, command)
            }
            delete<Response.Ok, ID>("/{id}") { params ->
                controller<CalendarController>(this).removeCalendar(
                    params.id
                )
            }
            get<Response.Data<Calendars>>("") {
                controller<CalendarController>(this).calendars()
            }
        }
    }

    private fun vacationRouting(route: Route) {
        route.route("/vacations") {
            get<Response.Data<RemainingTime>, ID>("/{id}") { params ->
                controller<VacationController>(this).calculateVacation(params.id)
            }

            get<Response.Data<RemainingTimes>>("") {
                controller<VacationController>(this).calculateVacations()
            }
        }
    }

    private fun holidaysRouting(route: Route) {
        route.route("/holidays") {
            post<Response.Either<Response.Ok, Response.Errors>, AddHoliday>("") { command ->
                controller<CalendarController>(this).addHoliday(command)
            }

            post<Response.Either<Response.Ok, Response.Errors>, EditHoliday, ID>("/{id}") { command, params ->
                controller<CalendarController>(this).editHoliday(params.id, command)
            }

            deleteWithBody<Response.Either<Response.Ok, Response.Errors>, RemoveHoliday>("") { command ->
                controller<CalendarController>(this).removeHoliday(command)
            }

            get<Response.Data<Holidays>>("") {
                controller<CalendarController>(this).holidays()
            }
        }
    }

    internal data class ID(val id: UUID)
    internal data class UserId(val userId: UUID)
}


