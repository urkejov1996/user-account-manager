CREATE DATABASE [user_account];
GO

USE [user_account];
GO

CREATE TABLE [users] (
    [id] UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    [first_name] NVARCHAR(50),
    [last_name] NVARCHAR(50),
    [username] NVARCHAR(50) UNIQUE,
    [email] NVARCHAR(100) UNIQUE,
    [phone_number] INT,
    [address] NVARCHAR(255),
    [created_at] DATETIME DEFAULT GETDATE(),
    [updated_at] DATETIME DEFAULT GETDATE(),
  
);


CREATE TABLE [accounts] (
    [id] UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(), 
    [user_id] UNIQUEIDENTIFIER NOT NULL, 
    [balance] DECIMAL(18, 2) NOT NULL, 
    [created_at] DATETIME DEFAULT GETDATE(),
    [updated_at] DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Account_User FOREIGN KEY (user_id) REFERENCES [users] (id));