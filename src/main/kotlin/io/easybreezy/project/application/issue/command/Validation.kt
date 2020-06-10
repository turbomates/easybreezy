package io.easybreezy.project.application.issue.command

import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.functions.isNotBlank
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.project.model.issue.File
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validateForEach

class Validation {

    fun validateCommand(command: New): List<Error> {
        return validate(command) {
            validate(New::content).isNotBlank()
        }
    }

    fun validateCommand(command: Update): List<Error> {
        return validate(command) {
            validate(Update::text).isNotBlank()
        }
    }

    fun validateCommand(command: AddFiles): List<Error> {
        return validate(command) {
            validate(AddFiles::files).validateForEach {
                validate(File::name).isNotNull().isNotBlank()
                validate(File::encodedContent).isNotNull().isNotBlank()
                validate(File::extension).isNotNull().isNotBlank()
            }
        }
    }

    fun validateCommand(command: RemoveFile): List<Error> {
        return validate(command) {
            validate(RemoveFile::issueId).isNotNull()
            validate(RemoveFile::path).isNotNull().isNotBlank()
        }
    }
}
