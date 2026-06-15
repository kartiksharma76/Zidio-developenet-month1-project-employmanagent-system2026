# Project Annotations Report

This document lists all annotations used in the project, grouped by package and class, along with their import sources.

## Package: `com.Comp_Emp_Manage`
**Total Classes/Interfaces:** 1

### Class: `CompEmpManageApplication`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@ComponentScan` | `org.springframework.context.annotation.ComponentScan` |
| `@SpringBootApplication` | `org.springframework.boot.autoconfigure.SpringBootApplication` |

## Package: `com.Comp_Emp_Manage.auth_service.Controller`
**Total Classes/Interfaces:** 2

### Class: `AuthController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Autowired` | `org.springframework.beans.factory.annotation.Autowired` |
| `@CrossOrigin` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PostMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestBody` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RestController` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |

### Class: `WebAuthController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Controller` | `org.springframework.stereotype.Controller` |
| `@GetMapping` | `org.springframework.web.bind.annotation.GetMapping` |
| `@ModelAttribute` | `org.springframework.web.bind.annotation.ModelAttribute` |
| `@PostMapping` | `org.springframework.web.bind.annotation.PostMapping` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

## Package: `com.Comp_Emp_Manage.auth_service.Dto`
**Total Classes/Interfaces:** 3

### Class: `AuthRequest`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@AllArgsConstructor` | `lombok.AllArgsConstructor` |
| `@Builder` | `lombok.Builder` |
| `@Data` | `lombok.Data` |
| `@NoArgsConstructor` | `lombok.NoArgsConstructor` |

### Class: `AuthResponse`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Builder` | `lombok.Builder` |
| `@Data` | `lombok.Data` |

### Class: `SignupRequest`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Data` | `lombok.Data` |

## Package: `com.Comp_Emp_Manage.auth_service.Entity`
**Total Classes/Interfaces:** 2

### Class: `Role`
- *No annotations used.*

### Class: `UserAuth`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@AllArgsConstructor` | `lombok.AllArgsConstructor` |
| `@Builder` | `lombok.Builder` |
| `@Column` | `Possible wildcard match: jakarta.persistence.*` |
| `@Data` | `lombok.Data` |
| `@Entity` | `Possible wildcard match: jakarta.persistence.*` |
| `@Enumerated` | `Possible wildcard match: jakarta.persistence.*` |
| `@GeneratedValue` | `Possible wildcard match: jakarta.persistence.*` |
| `@Id` | `Possible wildcard match: jakarta.persistence.*` |
| `@NoArgsConstructor` | `lombok.NoArgsConstructor` |
| `@Table` | `Possible wildcard match: jakarta.persistence.*` |

## Package: `com.Comp_Emp_Manage.auth_service.Repository`
**Total Classes/Interfaces:** 1

### Class: `UserAuthRepository`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Repository` | `org.springframework.stereotype.Repository` |

## Package: `com.Comp_Emp_Manage.auth_service.Security`
**Total Classes/Interfaces:** 4

### Class: `CustomUserDetailsService`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Override` | `java.lang.Override` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@Service` | `org.springframework.stereotype.Service` |

### Class: `JwtAuthenticationFilter`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Component` | `org.springframework.stereotype.Component` |
| `@Override` | `java.lang.Override` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

### Class: `JwtTokenProvider`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Component` | `org.springframework.stereotype.Component` |
| `@Value` | `org.springframework.beans.factory.annotation.Value` |

### Class: `SecurityConfig`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Bean` | `org.springframework.context.annotation.Bean` |
| `@Configuration` | `org.springframework.context.annotation.Configuration` |
| `@EnableMethodSecurity` | `org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity` |
| `@EnableWebSecurity` | `org.springframework.security.config.annotation.web.configuration.EnableWebSecurity` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

## Package: `com.Comp_Emp_Manage.auth_service.Service`
**Total Classes/Interfaces:** 1

### Class: `AuthService`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@Service` | `org.springframework.stereotype.Service` |

## Package: `com.Comp_Emp_Manage.common`
**Total Classes/Interfaces:** 1

### Class: `GlobalControllerAdvice`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@ControllerAdvice` | `org.springframework.web.bind.annotation.ControllerAdvice` |
| `@ModelAttribute` | `org.springframework.web.bind.annotation.ModelAttribute` |

## Package: `com.Comp_Emp_Manage.common.Config`
**Total Classes/Interfaces:** 3

### Class: `CorsConfig`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Configuration` | `org.springframework.context.annotation.Configuration` |
| `@Override` | `java.lang.Override` |

### Class: `DatabaseSeeder`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Bean` | `org.springframework.context.annotation.Bean` |
| `@Configuration` | `org.springframework.context.annotation.Configuration` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

### Class: `WebSocketConfig`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Configuration` | `org.springframework.context.annotation.Configuration` |
| `@EnableWebSocketMessageBroker` | `org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker` |
| `@MessageMapping` | `Unknown/Same Package/java.lang` |
| `@Override` | `java.lang.Override` |

## Package: `com.Comp_Emp_Manage.common.Controller`
**Total Classes/Interfaces:** 3

### Class: `AiController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@GetMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PathVariable` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@RestController` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |

### Class: `AnalyticsController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@GetMapping` | `org.springframework.web.bind.annotation.GetMapping` |
| `@RequestMapping` | `org.springframework.web.bind.annotation.RequestMapping` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@RestController` | `org.springframework.web.bind.annotation.RestController` |

### Class: `SkillController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@GetMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PathVariable` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@RestController` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |

## Package: `com.Comp_Emp_Manage.common.Service`
**Total Classes/Interfaces:** 3

### Class: `AiService`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Service` | `org.springframework.stereotype.Service` |
| `@Value` | `org.springframework.beans.factory.annotation.Value` |

### Class: `NotificationService`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@Service` | `org.springframework.stereotype.Service` |

### Class: `SkillService`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Service` | `org.springframework.stereotype.Service` |

## Package: `com.Comp_Emp_Manage.employee_service.Controller`
**Total Classes/Interfaces:** 16

### Class: `ApiDashboardController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@GetMapping` | `org.springframework.web.bind.annotation.GetMapping` |
| `@RequestMapping` | `org.springframework.web.bind.annotation.RequestMapping` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@RestController` | `org.springframework.web.bind.annotation.RestController` |

### Class: `AttendanceController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@GetMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PathVariable` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PostMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PutMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestParam` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@RestController` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |

### Class: `EmployeeController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@DeleteMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@GetMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PathVariable` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PostMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PreAuthorize` | `org.springframework.security.access.prepost.PreAuthorize` |
| `@PutMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestBody` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@RestController` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |

### Class: `EmployeeRegistrationDto`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Data` | `lombok.Data` |

### Class: `LeaveController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Data` | `lombok.Data` |
| `@GetMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PathVariable` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PostMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PutMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestBody` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestParam` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@RestController` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |

### Class: `LeaveRequestDto`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Data` | `lombok.Data` |

### Class: `PerformanceController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Data` | `lombok.Data` |
| `@GetMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PathVariable` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PostMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestBody` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@RestController` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |

### Class: `WebAiController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Controller` | `org.springframework.stereotype.Controller` |
| `@GetMapping` | `org.springframework.web.bind.annotation.GetMapping` |
| `@PostMapping` | `org.springframework.web.bind.annotation.PostMapping` |
| `@RequestBody` | `org.springframework.web.bind.annotation.RequestBody` |
| `@RequestMapping` | `org.springframework.web.bind.annotation.RequestMapping` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@ResponseBody` | `org.springframework.web.bind.annotation.ResponseBody` |
| `@Value` | `org.springframework.beans.factory.annotation.Value` |

### Class: `WebAnnouncementController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Controller` | `org.springframework.stereotype.Controller` |
| `@GetMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@ModelAttribute` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PathVariable` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PostMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

### Class: `WebAssetController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Controller` | `org.springframework.stereotype.Controller` |
| `@GetMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@ModelAttribute` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PathVariable` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PostMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestParam` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

### Class: `WebAttendanceController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Controller` | `org.springframework.stereotype.Controller` |
| `@PostMapping` | `org.springframework.web.bind.annotation.PostMapping` |
| `@RequestMapping` | `org.springframework.web.bind.annotation.RequestMapping` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

### Class: `WebDashboardController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Controller` | `org.springframework.stereotype.Controller` |
| `@GetMapping` | `org.springframework.web.bind.annotation.GetMapping` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

### Class: `WebEmployeeController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Controller` | `org.springframework.stereotype.Controller` |
| `@GetMapping` | `org.springframework.web.bind.annotation.GetMapping` |
| `@ModelAttribute` | `org.springframework.web.bind.annotation.ModelAttribute` |
| `@PathVariable` | `org.springframework.web.bind.annotation.PathVariable` |
| `@PostMapping` | `org.springframework.web.bind.annotation.PostMapping` |
| `@RequestParam` | `org.springframework.web.bind.annotation.RequestParam` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

### Class: `WebExpenseController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Controller` | `org.springframework.stereotype.Controller` |
| `@GetMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@ModelAttribute` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PathVariable` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PostMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestParam` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

### Class: `WebLeaveController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Controller` | `org.springframework.stereotype.Controller` |
| `@GetMapping` | `org.springframework.web.bind.annotation.GetMapping` |
| `@ModelAttribute` | `org.springframework.web.bind.annotation.ModelAttribute` |
| `@PostMapping` | `org.springframework.web.bind.annotation.PostMapping` |
| `@RequestMapping` | `org.springframework.web.bind.annotation.RequestMapping` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

### Class: `WebPerformanceController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Controller` | `org.springframework.stereotype.Controller` |
| `@GetMapping` | `org.springframework.web.bind.annotation.GetMapping` |
| `@PostMapping` | `org.springframework.web.bind.annotation.PostMapping` |
| `@RequestMapping` | `org.springframework.web.bind.annotation.RequestMapping` |
| `@RequestParam` | `org.springframework.web.bind.annotation.RequestParam` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

## Package: `com.Comp_Emp_Manage.employee_service.Entity`
**Total Classes/Interfaces:** 7

### Class: `Announcement`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@AllArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@Builder` | `Possible wildcard match: lombok.*` |
| `@Column` | `Possible wildcard match: lombok.*` |
| `@Data` | `Possible wildcard match: lombok.*` |
| `@Entity` | `Possible wildcard match: lombok.*` |
| `@GeneratedValue` | `Possible wildcard match: lombok.*` |
| `@Id` | `Possible wildcard match: lombok.*` |
| `@NoArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@Table` | `Possible wildcard match: lombok.*` |

### Class: `Asset`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@AllArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@Builder` | `Possible wildcard match: lombok.*` |
| `@Column` | `Possible wildcard match: lombok.*` |
| `@Data` | `Possible wildcard match: lombok.*` |
| `@Entity` | `Possible wildcard match: lombok.*` |
| `@GeneratedValue` | `Possible wildcard match: lombok.*` |
| `@Id` | `Possible wildcard match: lombok.*` |
| `@JoinColumn` | `Possible wildcard match: lombok.*` |
| `@ManyToOne` | `Possible wildcard match: lombok.*` |
| `@NoArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@Table` | `Possible wildcard match: lombok.*` |

### Class: `Attendance`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@AllArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@Builder` | `Possible wildcard match: lombok.*` |
| `@Column` | `Possible wildcard match: lombok.*` |
| `@Data` | `Possible wildcard match: lombok.*` |
| `@Entity` | `Possible wildcard match: lombok.*` |
| `@GeneratedValue` | `Possible wildcard match: lombok.*` |
| `@Id` | `Possible wildcard match: lombok.*` |
| `@JoinColumn` | `Possible wildcard match: lombok.*` |
| `@ManyToOne` | `Possible wildcard match: lombok.*` |
| `@NoArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@Table` | `Possible wildcard match: lombok.*` |

### Class: `Employee`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@AllArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@Builder` | `Possible wildcard match: lombok.*` |
| `@Column` | `Possible wildcard match: lombok.*` |
| `@Data` | `Possible wildcard match: lombok.*` |
| `@Entity` | `Possible wildcard match: lombok.*` |
| `@GeneratedValue` | `Possible wildcard match: lombok.*` |
| `@Id` | `Possible wildcard match: lombok.*` |
| `@JoinColumn` | `Possible wildcard match: lombok.*` |
| `@NoArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@OneToOne` | `Possible wildcard match: lombok.*` |
| `@Table` | `Possible wildcard match: lombok.*` |

### Class: `Expense`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@AllArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@Builder` | `Possible wildcard match: lombok.*` |
| `@Column` | `Possible wildcard match: lombok.*` |
| `@Data` | `Possible wildcard match: lombok.*` |
| `@Entity` | `Possible wildcard match: lombok.*` |
| `@GeneratedValue` | `Possible wildcard match: lombok.*` |
| `@Id` | `Possible wildcard match: lombok.*` |
| `@JoinColumn` | `Possible wildcard match: lombok.*` |
| `@ManyToOne` | `Possible wildcard match: lombok.*` |
| `@NoArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@Table` | `Possible wildcard match: lombok.*` |

### Class: `Leave`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@AllArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@Builder` | `Possible wildcard match: lombok.*` |
| `@Column` | `Possible wildcard match: lombok.*` |
| `@Data` | `Possible wildcard match: lombok.*` |
| `@Entity` | `Possible wildcard match: lombok.*` |
| `@GeneratedValue` | `Possible wildcard match: lombok.*` |
| `@Id` | `Possible wildcard match: lombok.*` |
| `@JoinColumn` | `Possible wildcard match: lombok.*` |
| `@ManyToOne` | `Possible wildcard match: lombok.*` |
| `@NoArgsConstructor` | `Possible wildcard match: lombok.*` |
| `@Table` | `Possible wildcard match: lombok.*` |

### Class: `Performance`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@AllArgsConstructor` | `lombok.AllArgsConstructor` |
| `@Builder` | `lombok.Builder` |
| `@Column` | `Possible wildcard match: jakarta.persistence.*` |
| `@Data` | `lombok.Data` |
| `@Entity` | `Possible wildcard match: jakarta.persistence.*` |
| `@GeneratedValue` | `Possible wildcard match: jakarta.persistence.*` |
| `@Id` | `Possible wildcard match: jakarta.persistence.*` |
| `@JoinColumn` | `Possible wildcard match: jakarta.persistence.*` |
| `@ManyToOne` | `Possible wildcard match: jakarta.persistence.*` |
| `@NoArgsConstructor` | `lombok.NoArgsConstructor` |
| `@Table` | `Possible wildcard match: jakarta.persistence.*` |

## Package: `com.Comp_Emp_Manage.employee_service.Repository`
**Total Classes/Interfaces:** 7

### Class: `AnnouncementRepository`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Repository` | `org.springframework.stereotype.Repository` |

### Class: `AssetRepository`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Repository` | `org.springframework.stereotype.Repository` |

### Class: `AttendanceRepository`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Repository` | `org.springframework.stereotype.Repository` |

### Class: `EmployeeRepository`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Repository` | `org.springframework.stereotype.Repository` |

### Class: `ExpenseRepository`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Repository` | `org.springframework.stereotype.Repository` |

### Class: `LeaveRepository`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Repository` | `org.springframework.stereotype.Repository` |

### Class: `PerformanceRepository`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Repository` | `org.springframework.stereotype.Repository` |

## Package: `com.Comp_Emp_Manage.employee_service.Security`
**Total Classes/Interfaces:** 1

### Class: `EmployeeController`
- *No annotations used.*

## Package: `com.Comp_Emp_Manage.employee_service.Service`
**Total Classes/Interfaces:** 4

### Class: `AttendanceService`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@Service` | `org.springframework.stereotype.Service` |

### Class: `EmployeeService`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@Service` | `org.springframework.stereotype.Service` |

### Class: `LeaveService`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Service` | `org.springframework.stereotype.Service` |

### Class: `PerformanceService`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Service` | `org.springframework.stereotype.Service` |

## Package: `com.Comp_Emp_Manage.payroll_service.Controller`
**Total Classes/Interfaces:** 2

### Class: `PayrollController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@GetMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PathVariable` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PostMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PutMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestParam` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@RestController` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |

### Class: `WebPayrollController`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Controller` | `org.springframework.stereotype.Controller` |
| `@GetMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PathVariable` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@PostMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestMapping` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequestParam` | `Possible wildcard match: org.springframework.web.bind.annotation.*` |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |

## Package: `com.Comp_Emp_Manage.payroll_service.Entity`
**Total Classes/Interfaces:** 1

### Class: `Payroll`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@AllArgsConstructor` | `lombok.AllArgsConstructor` |
| `@Builder` | `lombok.Builder` |
| `@Column` | `Possible wildcard match: jakarta.persistence.*` |
| `@Data` | `lombok.Data` |
| `@Entity` | `Possible wildcard match: jakarta.persistence.*` |
| `@GeneratedValue` | `Possible wildcard match: jakarta.persistence.*` |
| `@Id` | `Possible wildcard match: jakarta.persistence.*` |
| `@JoinColumn` | `Possible wildcard match: jakarta.persistence.*` |
| `@ManyToOne` | `Possible wildcard match: jakarta.persistence.*` |
| `@NoArgsConstructor` | `lombok.NoArgsConstructor` |
| `@Table` | `Possible wildcard match: jakarta.persistence.*` |

## Package: `com.Comp_Emp_Manage.payroll_service.Repository`
**Total Classes/Interfaces:** 1

### Class: `PayrollRepository`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@Repository` | `org.springframework.stereotype.Repository` |

## Package: `com.Comp_Emp_Manage.payroll_service.Service`
**Total Classes/Interfaces:** 1

### Class: `PayrollService`
| Annotation | Linked From (Import) |
| :--- | :--- |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` |
| `@Service` | `org.springframework.stereotype.Service` |
