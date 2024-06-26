package Neo4j;

import org.neo4j.cypherdsl.core.Cypher;
import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.neo4j.cypherdsl.core.renderer.Renderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.DatabaseSelection;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;

@Configuration
public class DatabaseConfig {
	@Bean
	DatabaseSelectionProvider databaseSelectionProvider(@Value("${spring.data.neo4j.database}") String database) {
		return () -> {
			String neo4jVersion = System.getenv("NEO4J_VERSION");
			if (neo4jVersion == null || (neo4jVersion.startsWith("4") || neo4jVersion.startsWith("5"))) {
				return DatabaseSelection.byName(database);
			}
			return DatabaseSelection.undecided();
		};
	}

	@Bean
	org.neo4j.cypherdsl.core.renderer.Configuration cypherDslConfiguration() {

		var dialect = Dialect.DEFAULT;
		String neo4jVersion = System.getenv("NEO4J_VERSION");
		if (neo4jVersion == null || neo4jVersion.startsWith("5")) {
			dialect = Dialect.NEO4J_5;
		}

		return org.neo4j.cypherdsl.core.renderer.Configuration.newConfig()
			.withDialect(dialect).build();
	}
}
