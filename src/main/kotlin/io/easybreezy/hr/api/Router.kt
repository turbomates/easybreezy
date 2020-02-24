package io.easybreezy.hr.api

import com.google.inject.Inject
import io.easybreezy.hr.api.controller.AbsenceController
import io.easybreezy.hr.api.controller.LocationController
import io.easybreezy.hr.api.controller.ProfileController
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
import io.easybreezy.hr.application.location.AssignLocation
import io.easybreezy.hr.application.location.CreateLocation
import io.easybreezy.hr.application.location.EditUserLocation
import io.easybreezy.hr.application.location.queryobject.Locations
import io.easybreezy.hr.application.location.queryobject.UserLocation
import io.easybreezy.hr.application.location.queryobject.UserLocations
import io.easybreezy.hr.application.profile.command.UpdateContactDetails
import io.easybreezy.hr.application.profile.command.UpdateMessengers
import io.easybreezy.hr.application.profile.command.UpdatePersonalData
import io.easybreezy.hr.application.profile.queryobject.Profile
import io.easybreezy.infrastructure.ktor.EmptyParams
import io.easybreezy.infrastructure.ktor.GenericPipeline
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.ktor.Router
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.infrastructure.ktor.delete
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
                    profileRouting(this)
                    absencesRouting(this)
                    locationsRouting(this)
                }
            }
        }
    }


    private fun profileRouting(route: Route) {
        route.route("/profile") {
            get<Response.Data<Profile>>("") {
                controller<ProfileController>(this).show(
                    resolvePrincipal<UserPrincipal>()
                )
            }
            post<Response.Either<Response.Ok, Response.Errors>, UpdatePersonalData>("/personal-data") { command ->
                controller<ProfileController>(this).updatePersonalData(
                    resolvePrincipal<UserPrincipal>(),
                    command
                )
            }
            post<Response.Ok, UpdateMessengers>("/add-messengers") { command ->
                controller<ProfileController>(this).updateMessengers(
                    resolvePrincipal<UserPrincipal>(),
                    command
                )
            }
            post<Response.Ok, UpdateContactDetails>("/contact-details") { command ->
                controller<ProfileController>(this).updateContactDetails(
                    resolvePrincipal<UserPrincipal>(),
                    command
                )
            }
        }
    }


    private fun absencesRouting(route: Route) {
        route.route("/absences") {
            data class ID(val id: UUID)

            workingHours(this)

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


    private fun workingHours(route: Route) {
        data class ID(val id: UUID)

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
            data class ID(val id: UUID)

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
}
