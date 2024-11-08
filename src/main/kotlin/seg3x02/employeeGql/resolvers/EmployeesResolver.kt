package seg3x02.employeeGql.resolvers

import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.repository.EmployeesRepository
import seg3x02.employeeGql.resolvers.types.CreateEmployeeInput
import java.util.*

@Controller
class EmployeesResolver(
    private val employeeRepository: EmployeesRepository,
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

    @MutationMapping
    fun newEmployee(@Argument createEmployeeInput: CreateEmployeeInput): Employee {
        if (createEmployeeInput.name != null &&
            createEmployeeInput.dateOfBirth != null &&
            createEmployeeInput.city != null &&
            createEmployeeInput.salary != null
        ) {
            val employee = Employee(
                createEmployeeInput.name,
                createEmployeeInput.dateOfBirth,
                createEmployeeInput.city,
                createEmployeeInput.salary,
                createEmployeeInput.gender,
                createEmployeeInput.email
            )
            employee.id = UUID.randomUUID().toString()
            employeeRepository.save(employee)
            return employee
        } else {
            throw Exception("Invalid input")
        }
    }

    @MutationMapping
    fun updateEmployee(
        @Argument id: String,
        @Argument createEmployeeInput: CreateEmployeeInput
    ): Employee {
        val existingEmployee = employeeRepository.findById(id)
            .orElseThrow { Exception("Employee not found") }

        if (createEmployeeInput.name != null &&
            createEmployeeInput.dateOfBirth != null &&
            createEmployeeInput.city != null &&
            createEmployeeInput.salary != null
        ) {
            val updatedEmployee = Employee(
                createEmployeeInput.name,
                createEmployeeInput.dateOfBirth,
                createEmployeeInput.city,
                createEmployeeInput.salary,
                createEmployeeInput.gender,
                createEmployeeInput.email
            )
            updatedEmployee.id = existingEmployee.id
            return employeeRepository.save(updatedEmployee)
        } else {
            throw Exception("Invalid input")
        }
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