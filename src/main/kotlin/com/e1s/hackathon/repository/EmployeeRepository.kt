package com.e1s.hackathon.repository

import com.e1s.hackathon.model.EmployeeDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : MongoRepository<EmployeeDocument, String>
