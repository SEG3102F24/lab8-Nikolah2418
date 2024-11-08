package seg3x02.employeeapigraphql.resolvers

import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import seg3x02.employeeapigraphql.entity.Employee
import seg3x02.employeeapigraphql.repository.EmployeeRepository
import seg3x02.employeeapigraphql.resolvers.types.CreateEmployeeInput
import java.util.*

@Controller
class EmployeesResolver(
    private val employeeRepository: EmployeeRepository,
    private val mongoOperations: MongoOperations
) {
    
    @QueryMapping
    fun employees(): List<Employee> {
        return employeeRepository.findAll()
    }

    @QueryMapping
    fun employeeById(@Argument id: String): Employee? {
        return employeeRepository.findById(id).orElse(null)
    }

    @QueryMapping
    fun employeeByNumber(@Argument employeeNumber: String): Employee? {
        val query = Query()
        query.addCriteria(Criteria.where("employeeNumber").`is`(employeeNumber))
        return mongoOperations.findOne(query, Employee::class.java)
    }

    @MutationMapping
    fun newEmployee(@Argument createEmployeeInput: CreateEmployeeInput): Employee {
        if (createEmployeeInput.employeeNumber != null &&
            createEmployeeInput.firstName != null &&
            createEmployeeInput.lastName != null &&
            createEmployeeInput.email != null &&
            createEmployeeInput.birthDate != null &&
            createEmployeeInput.department != null &&
            createEmployeeInput.title != null) {
                
            val employee = Employee(
                createEmployeeInput.employeeNumber,
                createEmployeeInput.firstName,
                createEmployeeInput.lastName,
                createEmployeeInput.email,
                createEmployeeInput.phone,
                createEmployeeInput.birthDate,
                createEmployeeInput.department,
                createEmployeeInput.title
            )
            employee.id = UUID.randomUUID().toString()
            return employeeRepository.save(employee)
        } else {
            throw Exception("Invalid input")
        }
    }

    @MutationMapping
    fun updateEmployee(@Argument id: String, @Argument createEmployeeInput: CreateEmployeeInput): Employee {
        val employee = employeeRepository.findById(id).orElseThrow { Exception("Employee not found") }
        
        if (createEmployeeInput.employeeNumber != null) employee.employeeNumber = createEmployeeInput.employeeNumber
        if (createEmployeeInput.firstName != null) employee.firstName = createEmployeeInput.firstName
        if (createEmployeeInput.lastName != null) employee.lastName = createEmployeeInput.lastName
        if (createEmployeeInput.email != null) employee.email = createEmployeeInput.email
        createEmployeeInput.phone?.let { employee.phone = it }
        if (createEmployeeInput.birthDate != null) employee.birthDate = createEmployeeInput.birthDate
        if (createEmployeeInput.department != null) employee.department = createEmployeeInput.department
        if (createEmployeeInput.title != null) employee.title = createEmployeeInput.title
        
        return employeeRepository.save(employee)
    }

    @MutationMapping
    fun deleteEmployee(@Argument id: String): Boolean {
        return try {
            employeeRepository.deleteById(id)
            true
        } catch (e: Exception) {
            false
        }
    }
}