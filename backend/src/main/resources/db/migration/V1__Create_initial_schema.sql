-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100),
    avatar_url VARCHAR(500),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- CREATE index for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);

-- Observation projects table
CREATE TABLE observation_projects (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE,
    project_type VARCHAR(20) DEFAULT 'plant' CHECK (project_type IN ('plant', 'animal', 'weather', 'other')),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_observation_projects_user_id ON observation_projects(user_id);
CREATE INDEX idx_observation_projects_start_date ON observation_projects(start_date);

-- Observation records table
CREATE TABLE observation_records (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES observation_projects(id) ON DELETE CASCADE,
    observation_date DATE NOT NULL,
    memo TEXT CHECK (LENGTH(memo) <= 500),
    weather VARCHAR(10) CHECK (weather IN ('sunny', 'cloudy', 'rainy', 'snowy')),
    temperature DECIMAL(4,1) CHECK (temperature >= -50.0 AND temperature <= 60.0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(project_id, observation_date)
);

-- Create indexes
CREATE INDEX idx_observation_records_project_id ON observation_records(project_id);
CREATE INDEX idx_observation_records_date ON observation_records(observation_date);
CREATE INDEX idx_observation_records_project_date ON observation_records(project_id, observation_date);

-- Photos table
CREATE TABLE photos (
    id BIGSERIAL PRIMARY KEY,
    record_id BIGINT NOT NULL REFERENCES observation_records(id) ON DELETE CASCADE,
    cloudinary_public_id VARCHAR(255) NOT NULL,
    cloudinary_url VARCHAR(500) NOT NULL,
    original_filename VARCHAR(255),
    file_size BIGINT,
    upload_order INTEGER DEFAULT 1 CHECK (upload_order >= 1 AND upload_order <= 3),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_photos_record_id ON photos(record_id);
CREATE INDEX idx_photos_public_id ON photos(cloudinary_public_id);

-- Create updated_at trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_observation_projects_updated_at BEFORE UPDATE ON observation_projects
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_observation_records_updated_at BEFORE UPDATE ON observation_records
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();