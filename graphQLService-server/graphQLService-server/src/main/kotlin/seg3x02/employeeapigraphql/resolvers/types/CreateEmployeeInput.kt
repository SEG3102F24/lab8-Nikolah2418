package seg3x02.employeeapigraphql.resolvers.types

class CreateEmployeeInput(
    val employeeNumber: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val birthDate: String? = null,
    val department: String? = null,
    val title: String? = null
)