-- Problem Hub Seed Data
USE problemhub;

-- 1. Seed Users (admin123 and student123)
INSERT INTO users (id, name, email, password_hash, role) VALUES
(1, 'System Administrator', 'admin@problemhub.com', '$2a$10$7TV2nZ2F6qJpcmJNtON99OLCSYImXMd793dzptoh9bkY.PDJ9D3f6', 'ADMIN'),
(2, 'Alex Chen', 'student@problemhub.com', '$2a$10$7yiLsN1Rf3d6W5oHGHL4COgUZzthWt5IDtLMHfvmZ1xGnIrRvhGJe', 'STUDENT')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 2. Seed Technologies
INSERT INTO technologies (id, name) VALUES
(1, 'Java'),
(2, 'Spring Boot'),
(3, 'Angular'),
(4, 'React'),
(5, 'TypeScript'),
(6, 'Python'),
(7, 'FastAPI'),
(8, 'Node.js'),
(9, 'MySQL'),
(10, 'PostgreSQL'),
(11, 'Redis'),
(12, 'Docker'),
(13, 'Kubernetes'),
(14, 'TensorFlow'),
(15, 'PyTorch'),
(16, 'AWS'),
(17, 'Flutter'),
(18, 'Kafka'),
(19, 'Elasticsearch'),
(20, 'OpenCV')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 3. Seed Tags
INSERT INTO tags (id, name) VALUES
(1, 'Machine Learning'),
(2, 'Healthcare'),
(3, 'FinTech'),
(4, 'EdTech'),
(5, 'Sustainability'),
(6, 'CleanTech'),
(7, 'Logistics'),
(8, 'Civic Tech'),
(9, 'Cybersecurity'),
(10, 'IoT'),
(11, 'Computer Vision'),
(12, 'NLP'),
(13, 'Mobile'),
(14, 'Cloud Native'),
(15, 'Real-Time'),
(16, 'Automation'),
(17, 'Open Source'),
(18, 'Accessibility'),
(19, 'Data Pipeline'),
(20, 'Microservices')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 4. Seed 20 Curated Real-World Problems
INSERT INTO problems (id, title, description, domain, difficulty, project_type, impact, solution_direction, expected_outcome, status, created_by) VALUES
(1, 
 'Cold-Chain Temperature Anomaly Detection for Insulin & Vaccines', 
 'Over 25% of temperature-sensitive biologics spoil before reaching patients in rural transit due to silent refrigeration unit failures and delayed telemetry reporting.', 
 'Healthcare', 
 'INTERMEDIATE', 
 'MAJOR_PROJECT', 
 'Eliminates biological waste, protects vulnerable patient populations, and provides auditable transit verification.', 
 'Build a low-cost IoT telemetry ingestion pipeline with edge timestamping, Redis pub/sub for threshold alerts, and automated SMS/push escalation.', 
 'Functional prototype with real-time temperature graph, breach event ledger, and simulated sensor stream emulator.', 
 'PUBLISHED', 1),

(2, 
 'Automated Micro-Lending Risk Scoring for Unbanked Artisans', 
 'Informal economy artisans and local traders cannot access formal financial credit due to lack of traditional credit histories, forcing them to rely on predatory lenders.', 
 'FinTech', 
 'ADVANCED', 
 'FINAL_YEAR_PROJECT', 
 'Democratizes capital access for micro-entrepreneurs and reduces default risk for community credit unions.', 
 'Develop an explainable risk-scoring engine utilizing alternative data points such as utility payment consistency, supplier transaction records, and inventory churn.', 
 'API that outputs risk tiers with SHAP feature contribution explanations and a borrower assessment dashboard.', 
 'PUBLISHED', 1),

(3, 
 'Campus Food Recovery & Redistribution Network', 
 'University cafeterias and campus events discard hundreds of kilograms of safe, edible food daily while food insecurity affects 1 in 3 tertiary students.', 
 'Civic Tech', 
 'BEGINNER', 
 'HACKATHON', 
 'Reduces municipal organic landfill methane emissions while providing confidential, dignified nutrition support to peers.', 
 'A lightweight PWA enabling cafeteria managers to post surplus batches with claim windows, routing notifications to student pantry coordinators.', 
 'Responsive web app with role-based claim flow, push notifications, and daily rescue metrics tracker.', 
 'PUBLISHED', 1),

(4, 
 'Interactive Dyslexia-Friendly Code Reader and Annotator', 
 'Neurodiverse programming students often experience extreme visual fatigue and syntax disorientation when parsing standard IDE font stacks and deeply nested code blocks.', 
 'EdTech', 
 'BEGINNER', 
 'MINI_PROJECT', 
 'Lowers cognitive barriers in STEM education and enhances technical learning outcomes for neurodivergent learners.', 
 'A web-based source code viewer utilizing OpenDyslexic typography, configurable semantic scope tinting, visual indentation guides, and line-by-line audio synthesis.', 
 'Browser-based code viewer with GitHub gist import, custom rendering filters, and personal layout preferences.', 
 'PUBLISHED', 1),

(5, 
 'Distributed Rooftop Solar Yield Verification & P2P Energy Ledger', 
 'Homeowners with residential solar setups have no transparent, low-cost way to trade localized surplus energy with adjacent microgrid neighbors during peak sunlight hours.', 
 'CleanTech', 
 'ADVANCED', 
 'FINAL_YEAR_PROJECT', 
 'Incentivizes decentralized renewable adoption and mitigates localized distribution transformer overheating.', 
 'An event-driven platform that aggregates smart-meter telemetry, validates energy production proof, and balances bilateral consumption settling through an audit log.', 
 'Distributed ledger demo featuring live microgrid balances, automated bilateral clearing, and grid export telemetry.', 
 'PUBLISHED', 1),

(6, 
 'Multi-Modal Emergency Dispatch Routing during Urban Flooding', 
 'Flash urban floods submerge primary transit arteries within minutes, trapping standard navigation ambulances in impassable waterlogged underpasses.', 
 'Civic Tech', 
 'ADVANCED', 
 'MAJOR_PROJECT', 
 'Reduces critical ambulance response times during extreme weather events and saves lives.', 
 'Integrate civic water-level sensors and crowdsourced road flood reports into a dynamic cost-weighted routing graph (Dijkstra/A* with dynamic node penalties).', 
 'Dispatcher dashboard displaying live flood inundation maps and dynamic emergency transit rerouting.', 
 'PUBLISHED', 1),

(7, 
 'Open API Security Policy & PII Leak Scanner for DevSecOps', 
 'Fast-moving backend teams frequently expose sensitive customer PII through unversioned REST API response payloads and improperly filtered log streams.', 
 'Cybersecurity', 
 'INTERMEDIATE', 
 'MAJOR_PROJECT', 
 'Prevents regulatory GDPR/HIPAA compliance penalties and protects consumer privacy before deployment to production.', 
 'Develop a CI/CD pipeline scanner that parses OpenAPI specs, generates dynamic fuzzing payloads, and inspects API responses against Regex/entropy PII signatures.', 
 'CLI tool and GitHub Action that generates vulnerability audit summaries and fails builds on critical PII exposures.', 
 'PUBLISHED', 1),

(8, 
 'Smart Precision Irrigation Scheduling using Soil Moisture Telemetry', 
 'Smallholder farmers experience 40% crop yield reduction due to either chronic under-watering or root rot from over-watering with arbitrary timer-based pumps.', 
 'Agriculture', 
 'INTERMEDIATE', 
 'MINI_PROJECT', 
 'Conserves up to 35% agricultural freshwater and boosts harvest predictability.', 
 'Combine capacitive soil sensor readings with localized weather forecast APIs to calculate evapotranspiration deficit and schedule automated pump relays.', 
 'Web dashboard displaying live soil moisture curves, weather outlook, and relay trigger schedule.', 
 'PUBLISHED', 1),

(9, 
 'Real-Time Drug Interaction & Allergy Conflict Checker for Rural Clinics', 
 'Under-resourced rural clinics operating with paper records lack automated safeguards against lethal polypharmacy contraindications and patient drug allergies.', 
 'Healthcare', 
 'INTERMEDIATE', 
 'HACKATHON', 
 'Eliminates preventable adverse drug events in primary clinics without high-speed internet.', 
 'A lightweight offline-first progressive web app querying an embedded SQLite/IndexedDB clinical drug conflict database with immediate contraindication alerts.', 
 'Offline-capable prescription validation tool with multi-drug interaction warnings and dosage validation.', 
 'PUBLISHED', 1),

(10, 
 'Hyperlocal E-Commerce Delivery Route Optimizer for Cargo Bikes', 
 'Urban last-mile delivery fleets struggle with narrow alleys, pedestrian zones, and one-way streets where traditional automotive GPS engines fail or create safety hazards.', 
 'Logistics', 
 'INTERMEDIATE', 
 'MAJOR_PROJECT', 
 'Lowers carbon footprint, slashes urban delivery delays, and improves courier safety.', 
 'Graph optimization engine tailored for pedestrian and bike paths that clusters delivery packages by spatial density and computes optimal multi-stop tours.', 
 'Courier mobile web app with turn-by-turn route sequencing and parcel handover scanner.', 
 'PUBLISHED', 1),

(11, 
 'Automated Grant Eligibility Matcher for Non-Profit Organizations', 
 'Grassroots community charities spend up to 200 hours per quarter searching through dense government PDFs to find eligible civic grants before deadlines expire.', 
 'Civic Tech', 
 'BEGINNER', 
 'MINI_PROJECT', 
 'Unlocks critical funding for grassroots civic causes by eliminating bureaucratic search friction.', 
 'A web crawler and NLP classifier that indexes municipal grant portals, parses eligibility requirements, and matches registered non-profit focus areas.', 
 'Searchable directory with eligibility filter, deadline calendar, and matching score indicators.', 
 'PUBLISHED', 1),

(12, 
 'Microservice Performance Anomaly Detector using Latency Traces', 
 'Engineering teams in distributed environments lose hours diagnosing intermittent latency spikes caused by cascading downstream service timeouts.', 
 'Cloud Native', 
 'ADVANCED', 
 'FINAL_YEAR_PROJECT', 
 'Reduces Mean Time to Detection (MTTD) from hours to seconds and prevents major SLA breaches.', 
 'Ingest OpenTelemetry spans, construct service dependency directed acyclic graphs, and apply statistical outlier detection to flag anomaly root causes.', 
 'Interactive trace visualization graph highlighting bottlenecks, tail latencies, and service error propagation.', 
 'PUBLISHED', 1),

(13, 
 'Personal Carbon Footprint Tracker from Digital Bank Statements', 
 'Consumers struggle to understand the ecological consequences of their daily consumption habits because receipt-level carbon footprint data is inaccessible.', 
 'Sustainability', 
 'INTERMEDIATE', 
 'MINI_PROJECT', 
 'Encourages proactive sustainable consumer purchasing behavior through transparent environmental feedback.', 
 'Parse uploaded CSV/PDF bank statements, categorize merchant codes (MCC), and map spending to carbon intensity emission factors.', 
 'Personal dashboard showing monthly carbon expenditure, category breakdowns, and reduction benchmarks.', 
 'PUBLISHED', 1),

(14, 
 'Automated Crop Disease Identification via Mobile Leaf Photos', 
 'Subsistence farmers lose up to 30% of staple crops each season because early leaf blight and pest infestations are misidentified or identified too late.', 
 'Agriculture', 
 'ADVANCED', 
 'MAJOR_PROJECT', 
 'Protects staple crop yields and informs timely, targeted organic or chemical interventions.', 
 'Train a lightweight convolutional vision model to classify common plant leaf diseases with confidence intervals and suggest localized treatments.', 
 'Mobile-first web app allowing users to upload leaf photos and receive immediate diagnosis with remediation steps.', 
 'PUBLISHED', 1),

(15, 
 'High-Volume Fraudulent Chargeback Detection for Digital Subscriptions', 
 'SaaS companies lose millions annually to "friendly fraud" chargebacks and stolen credit card testing bots targeting low-friction subscription signups.', 
 'FinTech', 
 'ADVANCED', 
 'FINAL_YEAR_PROJECT', 
 'Safeguards digital merchant merchant accounts from payment processor fines and preserves legitimate revenue.', 
 'Real-time rule engine and anomaly detection pipeline evaluating transaction velocity, device fingerprints, and IP reputation scores.', 
 'Transaction review queue with risk scoring scores, auto-flag triggers, and rule configuration panel.', 
 'PUBLISHED', 1),

(16, 
 'Public Transit Crowding Prediction & Live Capacity Monitor', 
 'Commuters face overcrowded buses and trains during unpredictable peak surges, while empty vehicles run immediately behind on identical routes.', 
 'Civic Tech', 
 'INTERMEDIATE', 
 'MAJOR_PROJECT', 
 'Improves passenger comfort, reduces transit wait times, and enables dynamic fleet reallocation.', 
 'Ingest tap-in/tap-out smart card timestamps alongside bus GPS coordinates to estimate passenger counts and forecast upcoming station congestion.', 
 'Live transit map showing bus occupancy levels, historical station congestion curves, and arrival predictions.', 
 'PUBLISHED', 1),

(17, 
 'Student Peer Code Review & Rubric Evaluation Portal', 
 'Computer science teaching assistants struggle to provide timely, granular code feedback across cohorts of 300+ students submitting weekly lab assignments.', 
 'EdTech', 
 'BEGINNER', 
 'MINI_PROJECT', 
 'Accelerates feedback loops, fosters collaborative code reading skills, and lightens teaching staff grading burden.', 
 'A web platform that facilitates double-blind peer review of code snippets based on structured rubric criteria and automated linter check reports.', 
 'Portal featuring anonymized submission review queues, inline commenting, and grade rubric consolidation.', 
 'PUBLISHED', 1),

(18, 
 'Container Image Vulnerability & Outdated Dependency Auditor', 
 'Small engineering startups often deploy Docker containers containing unpatched critical CVEs because commercial container security tools are cost-prohibitive.', 
 'Cybersecurity', 
 'BEGINNER', 
 'HACKATHON', 
 'Prevents common exploits targeting known base-image vulnerabilities in deployed cloud workloads.', 
 'Inspect container image layer manifests against public vulnerability databases (NVD/OSV) and provide actionable base-image upgrade recommendations.', 
 'Web interface displaying container CVE risk breakdown, affected packages, and minimal base-image alternatives.', 
 'PUBLISHED', 1),

(19, 
 'Hospital Medical Equipment Utilization & Preventive Maintenance Tracker', 
 'Large regional hospitals regularly suffer surgical procedure delays because specialized portable equipment (ultrasounds, infusion pumps) cannot be located or is overdue for maintenance.', 
 'Healthcare', 
 'INTERMEDIATE', 
 'MAJOR_PROJECT', 
 'Maximizes critical diagnostic hardware uptime and prevents surgical scheduling bottlenecks.', 
 'Equipment registry tracking maintenance lifecycle schedules, department transfers, calibration histories, and QR-code check-ins.', 
 'Hospital asset manager dashboard with calibration countdowns, repair request triage, and departmental transfer audit logs.', 
 'PUBLISHED', 1),

(20, 
 'Automated Open Source License Compliance Checker for Dependencies', 
 'Software teams unknowingly bundle viral GPL-licensed libraries into proprietary commercial applications, creating severe intellectual property liabilities.', 
 'Open Source', 
 'BEGINNER', 
 'HACKATHON', 
 'Ensures legal compliance and protects commercial software products from licensing disputes.', 
 'Scan project package files (pom.xml, package.json, requirements.txt), resolve dependency trees, and report license compatibility matrix.', 
 'Interactive web tool and API generating license compliance summaries and compatibility conflict flags.', 
 'PUBLISHED', 1)
ON DUPLICATE KEY UPDATE title=VALUES(title);

-- 5. Seed Problem Technologies Mapping
INSERT INTO problem_technologies (problem_id, technology_id) VALUES
-- Problem 1 (Cold-chain IoT): Java, Spring Boot, Redis, Docker
(1, 1), (1, 2), (1, 11), (1, 12),
-- Problem 2 (Micro-lending): Python, FastAPI, PostgreSQL, TypeScript
(2, 6), (2, 7), (2, 10), (2, 5),
-- Problem 3 (Food Recovery): Angular, TypeScript, Node.js, MySQL
(3, 3), (3, 5), (3, 8), (3, 9),
-- Problem 4 (Dyslexia Code Reader): Angular, TypeScript
(4, 3), (4, 5),
-- Problem 5 (Rooftop Solar): Java, Spring Boot, PostgreSQL, Kafka
(5, 1), (5, 2), (5, 10), (5, 18),
-- Problem 6 (Flood Emergency Routing): Python, FastAPI, PostgreSQL, Redis
(6, 6), (6, 7), (6, 10), (6, 11),
-- Problem 7 (API Security Scanner): Java, Spring Boot, Docker
(7, 1), (7, 2), (7, 12),
-- Problem 8 (Precision Irrigation): Python, FastAPI, MySQL
(8, 6), (8, 7), (8, 9),
-- Problem 9 (Drug Interaction Checker): Angular, TypeScript, Node.js
(9, 3), (9, 5), (9, 8),
-- Problem 10 (Cargo Bike Routing): TypeScript, Node.js, PostgreSQL
(10, 5), (10, 8), (10, 10),
-- Problem 11 (Grant Matcher): Python, FastAPI, MySQL
(11, 6), (11, 7), (11, 9),
-- Problem 12 (Microservice Latency): Java, Spring Boot, Kubernetes, Elasticsearch
(12, 1), (12, 2), (12, 13), (12, 19),
-- Problem 13 (Carbon Footprint): Angular, TypeScript, Python
(13, 3), (13, 5), (13, 6),
-- Problem 14 (Crop Disease Vision): Python, TensorFlow, PyTorch, OpenCV
(14, 6), (14, 14), (14, 15), (14, 20),
-- Problem 15 (Chargeback Fraud): Java, Spring Boot, Redis, Kafka
(15, 1), (15, 2), (15, 11), (15, 18),
-- Problem 16 (Transit Crowding): Python, FastAPI, PostgreSQL, Redis
(16, 6), (16, 7), (16, 10), (16, 11),
-- Problem 17 (Peer Code Review): Angular, TypeScript, Node.js, PostgreSQL
(17, 3), (17, 5), (17, 8), (17, 10),
-- Problem 18 (Container Vulnerability): Python, Docker, FastAPI
(18, 6), (18, 12), (18, 7),
-- Problem 19 (Medical Equipment): Java, Spring Boot, MySQL, Angular
(19, 1), (19, 2), (19, 9), (19, 3),
-- Problem 20 (License Compliance): Java, Spring Boot, Angular
(20, 1), (20, 2), (20, 3)
ON DUPLICATE KEY UPDATE problem_id=VALUES(problem_id);

-- 6. Seed Problem Tags Mapping
INSERT INTO problem_tags (problem_id, tag_id) VALUES
(1, 2), (1, 10), (1, 15), (1, 7),
(2, 3), (2, 1), (2, 16),
(3, 8), (3, 5), (3, 18),
(4, 4), (4, 18),
(5, 5), (5, 6), (5, 15),
(6, 8), (6, 15), (6, 7),
(7, 9), (7, 14), (7, 16),
(8, 10), (8, 5), (8, 16),
(9, 2), (9, 18), (9, 15),
(10, 7), (10, 5), (10, 16),
(11, 8), (11, 12), (11, 16),
(12, 14), (12, 15), (12, 20),
(13, 5), (13, 3), (13, 19),
(14, 1), (14, 11), (14, 16),
(15, 3), (15, 15), (15, 16),
(16, 8), (16, 15), (16, 19),
(17, 4), (17, 17), (17, 18),
(18, 9), (18, 12), (18, 14),
(19, 2), (19, 7), (19, 16),
(20, 17), (20, 9), (20, 16)
ON DUPLICATE KEY UPDATE problem_id=VALUES(problem_id);

-- 7. Seed Sample Bookmarks for Student (User 2)
INSERT INTO bookmarks (user_id, problem_id) VALUES
(2, 1),
(2, 4),
(2, 9)
ON DUPLICATE KEY UPDATE user_id=VALUES(user_id);
