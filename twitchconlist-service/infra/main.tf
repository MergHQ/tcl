provider "aws" {
  region = "eu-central-1"
}

variable "cluster_name" {
  type = string
}

variable "subnet1" {
  type = string
}

variable "subnet2" {
  type = string
}

variable "vpc" {
  type = string
}

variable "security_group" {
  type = string
}

variable "twitch_token" {
  type = string
}

variable "aws_token_id" {
  type = string
}

variable "aws_token_secret" {
  type = string
}


resource "aws_ecs_task_definition" "web" {
  family = "tcl-task"
  network_mode = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu = 128
  memory = 512

  container_definitions = <<DEFINITION
[
  {
    "name": "tcl-container",
    "essential": true,
    "image": "",
    "cpu": 128,
    "memory": null,
    "memoryReservation": null,
    "portMappings": [{
      "protocol": "tcp",
      "hostPort": 80,
      "containerPort": 80
    }],
    "secrets": [
      {"name": "TWITCH_API_TOKEN", "valueFrom": "${var.twitch_token}"},
      {"name": "AWS_TOKEN_ID", "valueFrom": "${var.aws_token_id}"},
      {"name": "AWS_TOKEN_SECRET", "valueFrom": "${var.aws_token_secret}"}
    ]
  }
]
DEFINITION
}

resource "aws_ecs_service" "tcl_service" {
  name            = "tcl-service"
  cluster         = "${var.cluster_name}"
  task_definition = "${aws_ecs_task_definition.web.arn}"
  desired_count   = 1

  network_configuration {
    security_groups = [var.security_group]
    subnets = [
      var.subnet1,
      var.subnet2
    ]
  }
}
