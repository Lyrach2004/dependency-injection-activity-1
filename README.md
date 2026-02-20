# 🚀 Rapport - Activité Pratique 1 : Injection des Dépendances & Framework IoC

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

*Université - GLSID S4 - JEE Middlewares*

**Auteur :** ZOUNGRANA Charly  
**Date :** 20 Février 2026  
**Repository GitHub :** https://github.com/Lyrach2004/dependency-injection-activity-1.git

</div>

---

## 📋 Objectif Pédagogique

Cette activité pratique vise à maîtriser **l'inversion de contrôle (IoC)** et **l'injection des dépendances** à travers :

- 🎯 **Partie 1** : Implémentation manuelle et utilisation du framework Spring
- 🔧 **Partie 2** : Conception d'un mini-framework IoC personnalisé

**Ressource vidéo de référence :** [📺 Tutoriel YouTube](https://www.youtube.com/watch?v=vOLqabN-n2k)

---

## 🏗️ Architecture du Projet

```
activité-pratique-1/
├── 📁 partie-1-spring/IOC/          # Spring & Injection Manuelle
│   ├── 📁 src/main/java/net/lyrach/
│   │   ├── 📁 dao/                  # Couche d'accès aux données
│   │   ├── 📁 metier/               # Couche métier
│   │   ├── 📁 presentation/         # Démonstrations
│   │   └── 📁 ext/                  # Extensions
│   └── 📁 src/main/resources/
└── 📁 partie-2-mini-framework/mini-ioc-framework/  # Framework IoC Personnalisé
    ├── 📁 src/main/java/
    │   ├── 📁 net/lyrach/           # Cœur du framework
    │   │   ├── 📁 core/             # Conteneur IoC et définitions
    │   │   ├── 📁 xml/              # Configuration XML (JAXB/OXM)
    │   │   └── 📁 annotation/       # Annotations personnalisées
    │   └── 📁 demo/                  # Cas d'usage et démonstrations
    │       ├── 📁 dao/              # DAO de démonstration
    │       ├── 📁 metier/           # Métier de démonstration
    │       └── 📁 presentation/     # Classes de test
    └── 📁 src/main/resources/
```

---

## 🎯 Partie 1 - Spring & Injection Manuelle

### 📝 Interfaces et Implémentations

#### Interface DAO
```java
public interface IDao {
    double getValue();  // Méthode d'accès aux données
}
```

#### Interface Métier
```java
public interface IMetier {
    double calcul();    // Méthode métier utilisant le DAO
}
```

### 🔄 Techniques d'Injection

#### 🏗️ 1. Instanciation Statique (Couplage Fort)
```java
// Création directe des dépendances
DaoImpl dao = new DaoImpl();
MetierImpl metier = new MetierImpl(dao);
System.out.println("Résultat=" + metier.calcul());
```
**Caractéristiques :**
- ❌ Couplage fort entre classes
- ✅ Performance optimale
- 🔧 Maintenance difficile

#### ⚡ 2. Instanciation Dynamique (Réflexion)
```java
// Configuration via fichier config.txt
Scanner sc = new Scanner(new File("config.txt"));
String daoClassName = sc.nextLine();     // net.lyrach.ext.DaoImplV2
String metierClassName = sc.nextLine();  // net.lyrach.metier.MetierImpl

// Instanciation par réflexion
Class<?> cDao = Class.forName(daoClassName);
IDao dao = (IDao) cDao.getConstructor().newInstance();

// Injection par setter
Method setDao = cMetier.getDeclaredMethod("setDao", IDao.class);
setDao.invoke(metier, dao);
```
**Caractéristiques :**
- ✅ Couplage faible
- ✅ Configuration externe
- ⚡ Performance réduite (réflexion)

#### 🌱 3. Spring Framework

**Version XML :**
```xml
<beans xmlns="http://www.springframework.org/schema/beans">
    <bean id="dao" class="net.lyrach.dao.DaoImpl"/>
    <bean id="metier" class="net.lyrach.metier.MetierImpl">
        <constructor-arg ref="dao"/>
    </bean>
</beans>
```

**Version Annotations :**
```java
@Component
public class MetierImpl implements IMetier {
    private IDao dao;
    
    public MetierImpl(@Qualifier("d2") IDao dao) {
        this.dao = dao;
    }
    
    @Override
    public double calcul() {
        double data = dao.getValue();
        return data * 67 * Math.PI;  // Traitement métier
    }
}
```

### 📊 Dépendances Maven

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>6.2.16</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>6.2.16</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
            <version>6.2.16</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>

</project>
```

---

## 🔧 Partie 2 - Mini Framework IoC Personnalisé

### 🎨 Fonctionnalités Développées

#### 🏛️ Cœur du Framework
- **`SimpleApplicationContext`** : Conteneur IoC principal
  - Gestion des cycles de vie des beans
  - Injection automatique des dépendances
  - Support des scopes (singleton par défaut)
- **`BeanDefinition`** : Définition des beans
  - Métadonnées de configuration
  - Informations sur les dépendances
- **`BeanFactory`** : Interface de fabrique
  - Abstraction de la création des beans
  - Support des différents types d'injection

#### 📄 Configuration XML (JAXB/OXM)
```xml
<beans>
    <bean id="dao" class="demo.ext.DaoImplV2"/>
    
    <bean id="metier" class="demo.metier.MetierImpl">
        <!-- Injection par constructeur -->
        <constructor-arg ref="dao"/>
        
        <!-- Injection par setter (commenté) -->
        <!-- <property name="dao" ref="dao"/> -->
        
        <!-- Injection par champ (commenté) -->
        <!-- <field name="dao" ref="dao"/> -->
    </bean>
</beans>
```

**Technologies utilisées :**
- **JAXB** : Java Architecture for XML Binding
- **OXM** : Object XML Mapping
- **Parsing automatique** des configurations

#### 🔍 Analyse Statique et Scanning
```java
// Scanning automatique des composants
ctx.loadAnnotatedBeans("demo");

// Processus :
// 1. Scan des classes du package
// 2. Détection des annotations @Component
// 3. Création des BeanDefinitions
// 4. Injection des dépendances @Autowired
// 5. Gestion des @Qualifier pour résoudre les ambiguïtés
```

#### 🏷️ Annotations Personnalisées
```java
// Marque une classe comme composant géré par le conteneur
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
    String value() default "";  // Nom du bean (optionnel)
}

// Injection automatique des dépendances
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface Autowired {
}

// Qualification pour lever les ambiguïtés
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface Qualifier {
    String value();
}
```

#### 🎯 Utilisation du Framework
```java
// Version XML
SimpleApplicationContext ctx = new SimpleApplicationContext();
ctx.loadBeansFromXml("config.xml");
IMetier metier = (IMetier) ctx.getBean("metier");

// Version Annotations
SimpleApplicationContext ctx = new SimpleApplicationContext();
ctx.loadAnnotatedBeans("demo");  // Scan du package
IMetier metier = (IMetier) ctx.getBean("metier");
```

### 🔄 Types d'Injection Supportés

#### 🏗️ Injection par Constructeur
```java
// Configuration XML
<bean id="metier" class="demo.metier.MetierImpl">
    <constructor-arg ref="dao"/>
</bean>

// Version Annotations
@Component
public class MetierImpl implements IMetier {
    @Autowired
    public MetierImpl(IDao dao) { this.dao = dao; }
}
```

#### ⚙️ Injection par Setter
```java
// Configuration XML
<bean id="metier" class="demo.metier.MetierImpl">
    <property name="dao" ref="dao"/>
</bean>

// Version Annotations
@Component
public class MetierImpl implements IMetier {
    @Autowired
    public void setDao(IDao dao) { this.dao = dao; }
}
```

#### 🎯 Injection par Attribut (Field)
```java
// Configuration XML
<bean id="metier" class="demo.metier.MetierImpl">
    <field name="dao" ref="dao"/>
</bean>

// Version Annotations
@Component
public class MetierImpl implements IMetier {
    @Autowired
    private IDao dao;  // Injection directe dans le champ
}
```

---


## 🏆 Résultats Obtenus

### 📊 Fonctionnalités Opérationnelles

| Partie | Fonctionnalité | Statut |
|--------|---------------|--------|
| **1** | Instanciation statique | ✅ |
| **1** | Instanciation dynamique | ✅ |
| **1** | Spring XML | ✅ |
| **1** | Spring Annotations | ✅ |
| **2** | Framework XML | ✅ |
| **2** | Framework Annotations | ✅ |
| **2** | Injection constructeur | ✅ |
| **2** | Injection setter | ✅ |
| **2** | Injection field | ✅ |

### 🎯 Compétences Acquises

#### 🏗️ Architecture & Design Patterns
- **Architecture en couches** avec séparation des responsabilités
- **Pattern Dependency Injection** : Inversion du contrôle
- **Pattern Factory** : Création centralisée des objets
- **Pattern Singleton** : Gestion du cycle de vie des beans
- **Pattern Template Method** : Algorithmes d'injection configurables

#### ⚡ Techniques Avancées Java
- **Réflexion Java** : Introspection et manipulation dynamique
- **Annotations personnalisées** : Métaprogrammation
- **Généricité** : Types paramétrés pour la flexibilité
- **Design Patterns comportementaux** : Strategy, Template

#### 🌱 Frameworks & Technologies
- **Framework Spring** : Configuration XML et annotations
- **JAXB/OXM** : Mapping XML-Objet automatique
- **Maven** : Gestion des dépendances et build
- **Scanning de classpath** : Découverte automatique des composants

#### 🔧 Ingénierie Logicielle
- **Couplage faible** : Architecture modulaire et testable
- **Configuration externe** : Flexibilité et maintenabilité
- **Gestion des cycles de vie** : Création et destruction des beans
- **Résolution des dépendances** : Graphes d'objets complexes

---

## 🚀 Conclusion

Cette activité pratique a permis de **maîtriser en profondeur** l'injection des dépendances à travers :

1. **Implémentation manuelle** des différentes approches
2. **Utilisation experte** du framework Spring
3. **Conception complète** d'un mini-framework IoC fonctionnel

Le projet démontre une **compréhension solide** des concepts d'IoC et met en œuvre les **meilleures pratiques** d'architecture logicielle.

---

<div align="center">

**📧 Contact :** charlyzoungrana2004@gmail.com 


---


</div>
