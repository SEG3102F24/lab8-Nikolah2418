package seg3x02.employeeapigraphql.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "employee")
data class Employee(
    var employeeNumber: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phone: String?,
    var birthDate: String,
    var department: String,
    var title: String
) {
    @Id
    var id: String = ""
}