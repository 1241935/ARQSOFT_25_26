package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.model.SqlDataModels.SqlBook;

@Repository
interface SqlBookJpaRepository extends JpaRepository<SqlBook, Long> {
}
